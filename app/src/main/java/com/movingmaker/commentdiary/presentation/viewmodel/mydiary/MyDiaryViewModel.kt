package com.movingmaker.commentdiary.presentation.viewmodel.mydiary

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.util.DIARY_TYPE
import com.movingmaker.commentdiary.common.util.DateConverter
import com.movingmaker.commentdiary.common.util.Event
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.usecase.DeleteDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.EditDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.GetMonthCommentUseCase
import com.movingmaker.commentdiary.domain.usecase.GetMonthDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.SaveDiaryUseCase
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyDiaryViewModel @Inject constructor(
    private val saveDiaryUseCase: SaveDiaryUseCase,
    private val editDiaryUseCase: EditDiaryUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val getMonthDiaryUseCase: GetMonthDiaryUseCase,
    private val getMonthCommentUseCase: GetMonthCommentUseCase
) : ViewModel() {

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _snackMessage = MutableLiveData<Event<String>>()
    val snackMessage: LiveData<Event<String>>
        get() = _snackMessage

    private var _aloneDiary = MutableLiveData<List<CalendarDay>>()
    val aloneDiary: LiveData<List<CalendarDay>>
        get() = _aloneDiary

    private var _commentDiary = MutableLiveData<List<CalendarDay>>()
    val commentDiary: LiveData<List<CalendarDay>>
        get() = _commentDiary

    private var _monthDiaries = MutableLiveData<List<Diary>>()
    val monthDiaries: LiveData<List<Diary>>
        get() = _monthDiaries

    private var _selectedDiary = MutableLiveData<Diary?>()
    val selectedDiary: LiveData<Diary?>
        get() = _selectedDiary

    private var _dateDiaryText = MutableLiveData<String>().apply { value = "" }
    val dateDiaryText: LiveData<String>
        get() = _dateDiaryText

    private var _selectedYearMonth = MutableLiveData<String>().apply {
        value = DateConverter.ymFormat(DateConverter.getCodaToday())
    }
    val selectedYearMonth: LiveData<String>
        get() = _selectedYearMonth

    private var _selectedDate = MutableLiveData<String>().apply {
        value = DateConverter.ymdFormat(DateConverter.getCodaToday())
    }
    val selectedDate: LiveData<String>
        get() = _selectedDate

    private var _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    private var _haveDayMyComment = MutableLiveData<Boolean>()
    val haveDayMyComment: LiveData<Boolean>
        get() = _haveDayMyComment

    private var _pushDate = MutableLiveData<String>()
    val pushDate: LiveData<String>
        get() = _pushDate

    //api response
    private var _responseEditDiary = MutableLiveData<Response<IsSuccessResponse>>()
    val responseEditDiary: LiveData<Response<IsSuccessResponse>>
        get() = _responseEditDiary


    private fun setAloneDiary(list: List<CalendarDay>) {
        _aloneDiary.value = list
    }

    private fun setCommentDiary(list: List<CalendarDay>) {
        _commentDiary.value = list
    }

    private fun setMonthDiaries(list: List<Diary>) {

        Timber.d("setMonthDiaries: $list")
        val aloneDiary = ArrayList<CalendarDay>()
        val commentDiary = ArrayList<CalendarDay>()

        for (selectedDiary in list) {
            val ymd = selectedDiary.date
            val (year, month, day) = ymd.split('.').map { it.toInt() }
            if (selectedDiary.deliveryYN == 'Y') {
                if (selectedDiary.tempYN == 'N')
                    commentDiary.add(CalendarDay.from(year, month - 1, day))
            } else {
                aloneDiary.add(CalendarDay.from(year, month - 1, day))
            }
        }

        setCommentDiary(commentDiary.toList())
        setAloneDiary(aloneDiary.toList())
        _monthDiaries.value = list
    }

    //for diary data
    fun setSelectedDiary(diary: Diary?) {
        if (diary == null) {
            _selectedDiary.value = null
            _commentList.value = emptyList()
        } else {
            _selectedDiary.value = diary
            _commentList.value = diary.commentList ?: emptyList()
        }
    }


    fun setDeliveryYN(type: Char) {
        _selectedDiary.value!!.deliveryYN = type
    }

    fun setSelectedDate(date: String?) {
        _selectedDate.value = date
    }

    fun setSelectedYearMonth(date: String?) {
        _selectedYearMonth.value = date
    }

    fun setPushDate(date: String) {
        _pushDate.value = date
    }

    //for view
    fun setDateDiaryText(text: String) {
        _dateDiaryText.value = text
    }

    fun setHaveDayMyComment(isHave: Boolean) {
        _haveDayMyComment.value = isHave
    }

    fun deleteLocalReportedComment(commentId: Long) {
        //신고/차단 코멘트 삭제
        val newDiary = Diary(
            selectedDiary.value!!.id,
            selectedDiary.value!!.title,
            selectedDiary.value!!.content,
            selectedDiary.value!!.date,
            selectedDiary.value!!.deliveryYN,
            selectedDiary.value!!.tempYN,
            selectedDiary.value!!.commentList
        )
        for (idx in _selectedDiary.value!!.commentList!!.indices) {
            val comment = selectedDiary.value!!.commentList!![idx]
            if (comment.id == commentId) {
                newDiary.commentList!!.removeAt(idx)
                setSelectedDiary(newDiary)
                return
            }
        }
    }

    /*
    * write diary
    * */
    private var _selectedDiaryType = MutableLiveData<DIARY_TYPE>()
    val selectedDiaryType: LiveData<DIARY_TYPE>
        get() = _selectedDiaryType

    private var _selectDiaryTypeToolbarIsExpanded =
        MutableLiveData<Boolean>().apply { value = false }
    val selectDiaryTypeToolbarIsExpanded: LiveData<Boolean>
        get() = _selectDiaryTypeToolbarIsExpanded

    private var _diaryContentText = MutableLiveData<String>()
    val diaryContentText: LiveData<String>
        get() = _diaryContentText

    private var _diaryHeadText = MutableLiveData<String>()
    val diaryHeadText: LiveData<String>
        get() = _diaryHeadText

    private var _canSendCommentDiary = MutableLiveData<Boolean>().apply { value = false }
    val canSendCommentDiary: LiveData<Boolean>
        get() = _canSendCommentDiary

    fun setDiaryContentText(text: String) {
        _diaryContentText.value = text
        setCanSendCommentDiary()
    }

    fun setDiaryHeadText(text: String) {
        _diaryHeadText.value = text
        setCanSendCommentDiary()
    }

    fun setSelectedDiaryType(type: DIARY_TYPE) {
        _selectedDiaryType.value = type
    }

    fun selectDiaryType(view: View) {
        _selectDiaryTypeToolbarIsExpanded.value = false
        when (view.id) {
            R.id.selectCommentDiaryLayout -> {
                setSelectedDiaryType(DIARY_TYPE.COMMENT_DIARY)
            }
            R.id.selectAloneDiaryLayout -> {
                setSelectedDiaryType(DIARY_TYPE.ALONE_DIARY)
            }
        }
    }

    private fun setCanSendCommentDiary() {
        _canSendCommentDiary.value =
            if (diaryHeadText.value == null || _diaryContentText.value == null) {
                false
            } else {
                diaryHeadText.value!!.isNotBlank() && diaryContentText.value!!.length >= 100
            }
    }

    fun changeSelectDiaryTypeToolbarIsExpanded() {
        _selectDiaryTypeToolbarIsExpanded.value = _selectDiaryTypeToolbarIsExpanded.value!! xor true
    }

    fun closeSelectDiaryTypeToolbarIsExpanded() {
        _selectDiaryTypeToolbarIsExpanded.value = false
    }

    suspend fun saveDiary(deliveryYN: Char) = viewModelScope.async {
        var isSuccess = false
        _loading.postValue(true)
        val saveDiaryRequest: SaveDiaryRequest = SaveDiaryRequest(
            title = diaryHeadText.value ?: "",
            content = diaryContentText.value ?: "",
            date = selectedDate.value ?: "",
            deliveryYN = deliveryYN,
            tempYN = 'N',
        )
        with(saveDiaryUseCase(saveDiaryRequest)) {
            _loading.postValue(false)
            if (isSuccessful) {
                body()?.let { response ->
                    when (code()) {
                        200 -> {
                            Timber.d("Abc", "setResponseEditDiary: ${response}")
                            val newDiary = Diary(
                                response.result.id,
                                saveDiaryRequest.title,
                                saveDiaryRequest.content,
                                saveDiaryRequest.date,
                                saveDiaryRequest.deliveryYN,
                                saveDiaryRequest.tempYN,
                                null
                            )
                            setSelectedDiary(newDiary)
                            _snackMessage.postValue(Event("일기가 저장되었습니다."))
                            isSuccess = true
                        }
                        else -> {
                            _snackMessage.value = Event(message())
                        }
                    }
                }
            } else {
                errorBody()?.let { errorBody ->
//                    RetrofitClient.getErrorResponse(errorBody)?.let {
//                        if (it.status == 401) {
//                            onError("다시 로그인해 주세요.")
//                            CodaApplication.getInstance().logOut()
//                        } else {
//                            _snackMessage.postValue("일기 저장에 실패하였습니다.")
//                        }
//                    }
                }
            }
        }
        return@async isSuccess
    }.await()

    fun likeLocalComment(commentId: Long) {
        val newDiary = Diary(
            selectedDiary.value!!.id,
            selectedDiary.value!!.title,
            selectedDiary.value!!.content,
            selectedDiary.value!!.date,
            selectedDiary.value!!.deliveryYN,
            selectedDiary.value!!.tempYN,
            selectedDiary.value!!.commentList
        )
        //신고한 코멘트 삭
        for (idx in _selectedDiary.value!!.commentList!!.indices) {
            val comment = selectedDiary.value!!.commentList!![idx]
            if (comment.id == commentId) {
                //like -> dislike, dislike -> like
                val newComment = Comment(
                    comment.id,
                    comment.content,
                    comment.date,
                    !comment.like
                )
                newDiary.commentList!![idx] = newComment
//                _selectedDiary.value!!.commentList!![idx].like=true
                setSelectedDiary(newDiary)
                return
            }
        }
    }

    fun getMonthDiary(date: String) = viewModelScope.launch {
        _loading.postValue(true)
        with(getMonthDiaryUseCase(date)) {
            _loading.postValue(false)
            if (isSuccessful) {
                body()?.let { result ->
                    Timber.d("observedata", "setResponseGetMonthDiary: $result")
//                    Timber.d("code", "setResponseGetMonthDiary: code: ${it.code()}")
                    when (code()) {
                        200 -> {
                            setMonthDiaries(body()!!.result)
                        }
//                        else -> onError(message())
                    }
//                    Timber.d("viewmodel", "observerDatas: ${solvedProblems.value!!.size}")
                }
            } else {
                errorBody()?.let { errorBody ->
//                    RetrofitClient.getErrorResponse(errorBody)?.let {
//                        onError(it.message)
//                        Timber.d("viewmodel", "observerDatas: $it")
//                    }
                }
            }
        }
    }

    fun setResponseEditDiary(diaryId: Long, editDiaryRequest: EditDiaryRequest) =
        viewModelScope.launch {
            _loading.postValue(true)
            with(editDiaryUseCase(diaryId, editDiaryRequest)) {
                _loading.postValue(false)
                if (isSuccessful) {
                    body()?.let { response ->
                        when (code()) {
                            200 -> {
                                val newDiary = Diary(
                                    selectedDiary.value!!.id,
                                    editDiaryRequest.title,
                                    editDiaryRequest.content,
                                    selectedDiary.value!!.date,
                                    selectedDiary.value!!.deliveryYN,
                                    selectedDiary.value!!.tempYN,
                                    selectedDiary.value!!.commentList
                                )
                                setSelectedDiary(newDiary)
                                _snackMessage.postValue(Event("일기가 수정되었습니다."))
                            }
//                            else -> onError(message())
                        }
                    }
                } else {
                    errorBody()?.let { errorBody ->
//                        RetrofitClient.getErrorResponse(errorBody)?.let {
//                            if (it.status == 401) {
//                                onError("다시 로그인해 주세요.")
//                                CodaApplication.getInstance().logOut()
//                            } else {
//                                _snackMessage.postValue("일기 저장에 실패하였습니다.")
//                            }
//                        }
                    }
                }
            }
        }

    suspend fun deleteDiary() = viewModelScope.async {
        var isSuccess = false
        _loading.postValue(true)
        if (selectedDiary.value?.id != null) {
            with(deleteDiaryUseCase(selectedDiary.value!!.id!!)) {
                _loading.postValue(false)
                if (isSuccessful) {
                    body()?.let {
                        when (code()) {
                            200 -> {
                                _snackMessage.value = Event("일기가 삭제되었습니다.")
                                isSuccess = true
                            }
//                            else -> onError(message())
                        }
                    }
                } else {
                    errorBody()?.let { errorBody ->
//                        RetrofitClient.getErrorResponse(errorBody)?.let {
//                            if (it.status == 401) {
//                                onError("다시 로그인해 주세요.")
//                                CodaApplication.getInstance().logOut()
//                            } else {
//                                _snackMessage.postValue("일기 삭제에 실패하였습니다.")
//                            }
//                        }
                    }
                }
            }
        }
        return@async isSuccess
    }.await()

    fun setResponseGetDayComment(date: String) = viewModelScope.launch {
        onLoading()
        with(getMonthCommentUseCase(date)) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    setHaveDayMyComment(data.isEmpty())
                }
                is UiState.Error -> {
                    _snackMessage.value = Event(message)
                }
                is UiState.Fail -> {
                    _snackMessage.value = Event(message)
                }
            }
        }
    }

    private fun onLoading() {
        _loading.value = true
    }

    private fun offLoading() {
        _loading.value = false
    }

}