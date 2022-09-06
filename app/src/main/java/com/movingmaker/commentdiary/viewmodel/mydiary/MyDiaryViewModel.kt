package com.movingmaker.commentdiary.viewmodel.mydiary

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.remote.response.*
import com.movingmaker.commentdiary.data.repository.MyDiaryRepository
import com.movingmaker.commentdiary.data.repository.MyPageRepository
import com.movingmaker.commentdiary.util.DIARY_TYPE
import com.movingmaker.commentdiary.util.DateConverter
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.*
import retrofit2.Response
import java.util.*

class MyDiaryViewModel : ViewModel() {

    var job: Job? = null

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _snackMessage = MutableLiveData<String>()
    val snackMessage: LiveData<String>
        get() = _snackMessage

    //코루틴 예외처리 핸들러
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

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

    private var _dateDiaryText = MutableLiveData<String>()
    val dateDiaryText: LiveData<String>
        get() = _dateDiaryText

    private var _isDeletedDiary = MutableLiveData<Boolean>()
    val isDeletedDiary: LiveData<Boolean>
        get() = _isDeletedDiary

//    private var _isSavedDiary = MutableLiveData<Boolean>()
//    val isSavedDiary: LiveData<Boolean>
//        get() = _isSavedDiary
//
//    private var _isEditedDiary = MutableLiveData<Boolean>()
//    val isEditedDiary: LiveData<Boolean>
//        get() = _isEditedDiary

    private var _commentDiaryTextCount = MutableLiveData<Int>()
    val commentDiaryTextCount: LiveData<Int>
        get() = _commentDiaryTextCount

//    private var _saveOrEdit = MutableLiveData<String>()
//    val saveOrEdit: LiveData<String>
//        get() = _saveOrEdit

    private var _selectedYearMonth = MutableLiveData<String>().apply { value = DateConverter.ymFormat(DateConverter.getCodaToday()) }
    val selectedYearMonth: LiveData<String>
        get() = _selectedYearMonth

    private var _selectedDate = MutableLiveData<String>().apply { value = DateConverter.ymdFormat(DateConverter.getCodaToday())  }
    val selectedDate: LiveData<String?>
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

    private var _selectDiaryTypeToolbarIsExpanded = MutableLiveData<Boolean>().apply{value = false}
    val selectDiaryTypeToolbarIsExpanded: LiveData<Boolean>
        get() = _selectDiaryTypeToolbarIsExpanded

    //api response
    private var _responseEditDiary = MutableLiveData<Response<IsSuccessResponse>>()
    val responseEditDiary: LiveData<Response<IsSuccessResponse>>
        get() = _responseEditDiary

    private var _selectedDiaryType = MutableLiveData<DIARY_TYPE>()
    val selectedDiaryType: LiveData<DIARY_TYPE>
        get() = _selectedDiaryType

    init {
//        _selectedDiary.value = Diary(null, "", "", "", ' ', ' ', null)
        _dateDiaryText.value = ""
        _commentDiaryTextCount.value = 0
    }

    private fun setAloneDiary(list: List<CalendarDay>) {
        _aloneDiary.value = list
    }

    private fun setCommentDiary(list: List<CalendarDay>) {
        _commentDiary.value = list
    }

    private fun setMonthDiaries(list: List<Diary>) {

        Log.d("observedata", "setMonthDiaries: $list")
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
        if(diary == null){
            _selectedDiary.value = null
            _commentList.value = null
        } else{
            _selectedDiary.value = diary
            _commentList.value = diary.commentList ?: emptyList()
        }
    }

    fun setSelectedDiaryType(type: DIARY_TYPE){
        _selectedDiaryType.value = type
    }

    fun selectDiaryType(view: View){
        _selectDiaryTypeToolbarIsExpanded.value = false
        when(view.id){
            R.id.selectCommentDiaryLayout -> {
                setSelectedDiaryType(DIARY_TYPE.COMMENT_DIARY)
            }
            R.id.selectAloneDiaryLayout -> {
                setSelectedDiaryType(DIARY_TYPE.ALONE_DIARY)
            }
        }
    }

    fun changeSelectDiaryTypeToolbarIsExpanded() {
        _selectDiaryTypeToolbarIsExpanded.value = _selectDiaryTypeToolbarIsExpanded.value!! xor true
    }

    fun setDeliveryYN(type: Char) {
//        _deliveryYN.value = type
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

    fun setCommentDiaryTextCount(count: Int) {
        _commentDiaryTextCount.value = count
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
        //        _responseGetSolvedProblems.value = SolvedacRepository.INSTANCE.getSolvedProblem(handle)
        var response: Response<DiaryListResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = MyDiaryRepository.INSTANCE.getMonthDiary(date)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    Log.d("observedata", "setResponseGetMonthDiary: $result")
//                    Log.d("code", "setResponseGetMonthDiary: code: ${it.code()}")
                    when (it.code()) {
                        200 -> {
                            setMonthDiaries(it.body()!!.result)
                        }
                        else -> onError(it.message())
                    }
//                    Log.d("viewmodel", "observerDatas: ${solvedProblems.value!!.size}")
                }
            } else {
                it.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        onError(it.message)
                        Log.d("viewmodel", "observerDatas: $it")
                    }
                }
            }
        }
    }

    fun setResponseSaveDiary(saveDiaryRequest: SaveDiaryRequest) = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<SaveDiaryResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = MyDiaryRepository.INSTANCE.saveDiary(saveDiaryRequest)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    Log.d("--> 시발", "setResponseSaveDiary: ${response.result}")
                    when (it.code()) {
                        200 -> {
                            val newDiary = Diary(
                                response.result.id,
                                saveDiaryRequest.title,
                                saveDiaryRequest.content,
                                saveDiaryRequest.date,
                                selectedDiary.value!!.deliveryYN,
                                selectedDiary.value!!.tempYN,
                                null
                            )
                            setSelectedDiary(newDiary)
                            //혼자 쓴 일기 저장할 때만 스낵바
                            if (selectedDiary.value!!.deliveryYN == 'N') {
                                _snackMessage.postValue("일기가 저장되었습니다.")
                            }
                        }
                        else -> onError(it.message())
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            onError("다시 로그인해 주세요.")
                            CodaApplication.getInstance().logOut()
                        } else {
                            _snackMessage.postValue("일기 저장에 실패하였습니다.")
                        }
                    }
                }
            }
        }
    }

    fun setResponseEditDiary(diaryId: Long, editDiaryRequest: EditDiaryRequest) =
        viewModelScope.launch {

            _loading.postValue(true)
            var response: Response<IsSuccessResponse>? = null
            val job = launch(Dispatchers.Main + exceptionHandler) {
                response = MyDiaryRepository.INSTANCE.editDiary(diaryId, editDiaryRequest)
            }
            job.join()
            _loading.postValue(false)
            response?.let {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        when (it.code()) {
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
                                _snackMessage.postValue("일기가 수정되었습니다.")
                            }
                            else -> onError(it.message())
                        }
                    }
                } else {
                    it.errorBody()?.let { errorBody ->
                        RetrofitClient.getErrorResponse(errorBody)?.let {
                            if (it.status == 401) {
                                onError("다시 로그인해 주세요.")
                                CodaApplication.getInstance().logOut()
                            } else {
                                _snackMessage.postValue("일기 저장에 실패하였습니다.")
                            }
                        }
                    }
                }
            }


            withContext(viewModelScope.coroutineContext) {
                _responseEditDiary.value =
                    MyDiaryRepository.INSTANCE.editDiary(diaryId, editDiaryRequest)
            }
        }

    fun setResponseDeleteDiary(diaryId: Long) = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<IsSuccessResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = MyDiaryRepository.INSTANCE.deleteDiary(diaryId)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    when (it.code()) {
                        200 -> {
                            _snackMessage.postValue("일기가 삭제되었습니다.")
                            _isDeletedDiary.postValue(true)
                        }
                        else -> onError(it.message())
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            onError("다시 로그인해 주세요.")
                            CodaApplication.getInstance().logOut()
                        } else {
                            _snackMessage.postValue("일기 삭제에 실패하였습니다.")
                        }
                    }
                }
            }
        }
    }

    fun setResponseGetDayComment(date: String) = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<CommentListResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = MyPageRepository.INSTANCE.getMonthComment(date)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    when (it.code()) {
                        200 -> {
                            setHaveDayMyComment(result.result.isEmpty())
                        }
                        else -> onError(it.message())
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            onError("다시 로그인해 주세요.")
                            CodaApplication.getInstance().logOut()
                        } else {
                            _snackMessage.postValue("코멘트를 읽는 데 실패하였습니다.")
                        }
                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}