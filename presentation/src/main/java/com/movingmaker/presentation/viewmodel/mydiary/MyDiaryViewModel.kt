package com.movingmaker.presentation.viewmodel.mydiary

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.usecase.DeleteDiaryUseCase
import com.movingmaker.domain.usecase.EditDiaryUseCase
import com.movingmaker.domain.usecase.GetMonthDiaryUseCase
import com.movingmaker.domain.usecase.GetPeriodCommentUseCase
import com.movingmaker.domain.usecase.SaveDiaryUseCase
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseViewModel
import com.movingmaker.presentation.util.DIARY_TYPE
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymFormatForLocalDate
import com.movingmaker.presentation.util.ymdFormat
import com.movingmaker.presentation.util.ymdToCalendarDay
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyDiaryViewModel @Inject constructor(
    private val saveDiaryUseCase: SaveDiaryUseCase,
    private val editDiaryUseCase: EditDiaryUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val getMonthDiaryUseCase: GetMonthDiaryUseCase,
    private val getPeriodCommentUseCase: GetPeriodCommentUseCase,
) : BaseViewModel() {

    private var _aloneDiaryDates = MutableLiveData<List<CalendarDay>>()
    val aloneDiaryDates: LiveData<List<CalendarDay>>
        get() = _aloneDiaryDates

    private var _commentDiaryDates = MutableLiveData<List<CalendarDay>>()
    val commentDiaryDates: LiveData<List<CalendarDay>>
        get() = _commentDiaryDates

    private var _monthDiaries = MutableLiveData<List<Diary>>()
    val monthDiaries: LiveData<List<Diary>>
        get() = _monthDiaries

    private var _selectedDiary = MutableLiveData<Diary?>()
    val selectedDiary: LiveData<Diary?>
        get() = _selectedDiary

    private var _selectedYearMonth = MutableLiveData<String>().apply {
        value = ymFormatForLocalDate(getCodaToday())
    }
    val selectedYearMonth: LiveData<String>
        get() = _selectedYearMonth

    private var _selectedDate = MutableLiveData<String>().apply {
        value = ymdFormat(getCodaToday())
    }
    val selectedDate: LiveData<String>
        get() = _selectedDate

    private var _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    private var _haveDayWrittenComment = MutableLiveData<Boolean>()
    val haveDayMyComment: LiveData<Boolean>
        get() = _haveDayWrittenComment

    private var _pushDate = MutableLiveData<String>()
    val pushDate: LiveData<String>
        get() = _pushDate

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

    var diaryHead = MutableLiveData<String>().apply { value = "" }
    var diaryContent = MutableLiveData<String>().apply { value = "" }

    private var _canSendCommentDiary = MutableLiveData<Boolean>().apply { value = false }
    val canSendCommentDiary: LiveData<Boolean>
        get() = _canSendCommentDiary

    init {
        getMonthDiary(ymFormatForLocalDate(getCodaToday())!!)
    }

    /*
    * head, content 통합 한 글자라도 있으면 Save
    * 1. 현재 선택된 일기가 없다면 신규 작성 -> SaveDiaryModel
    * 2. 현재 선택된 일기가 있다면 수정 -> EditDiaryModel
    * 1에서 2로 가는 로직
    * head, content 통합 한 글자라도 없다면 delete
    * */
    val aloneDiary = MediatorLiveData<Any>().apply {
        addSource(selectedDiaryType) { diaryType ->
            removeSource(diaryHead)
            removeSource(diaryContent)
            combineDiaryHead(this)
            combineDiaryContent(this, diaryType)
        }
    }

    private fun combineDiaryHead(aloneDiary: MediatorLiveData<Any>) = with(aloneDiary) {
        addSource(diaryHead) { head ->
            //신규 작성
            value = if (selectedDiary.value == null) {
                getSaveAloneDiary(
                    head,
                    diaryContent.value ?: ""
                )
            } else {
                //수정
                getEditAloneDiary(
                    head,
                    diaryContent.value ?: ""
                )
            }
        }
    }

    private fun combineDiaryContent(
        aloneDiary: MediatorLiveData<Any>,
        diaryType: DIARY_TYPE
    ) = with(aloneDiary) {
        if (diaryType == DIARY_TYPE.ALONE_DIARY) {
            addSource(diaryContent) { content ->
                //신규 작성
                value = if (selectedDiary.value == null) {
                    getSaveAloneDiary(
                        diaryHead.value ?: "",
                        content
                    )
                } else {
                    //수정
                    getEditAloneDiary(
                        diaryHead.value ?: "",
                        content
                    )
                }
            }
        }
    }

    private fun getSaveAloneDiary(
        head: String,
        content: String
    ): SaveDiaryModel? {
        selectedDate.value?.let { date ->
            return SaveDiaryModel(
                head,
                content,
                date,
                'N'
            )
        }
        return null
    }

    private fun getEditAloneDiary(
        head: String,
        content: String
    ): EditDiaryModel {
        return EditDiaryModel(
            head,
            content,
        )
    }

    private fun setMonthDiaries(list: List<Diary>) {
        val aloneDiary = ArrayList<CalendarDay>()
        val commentDiary = ArrayList<CalendarDay>()

        for (selectedDiary in list) {
            ymdToCalendarDay(selectedDiary.date)?.let { date ->
                if (selectedDiary.deliveryYN == 'Y') {
                    commentDiary.add(date)
                } else {
                    aloneDiary.add(date)
                }
            }
        }

        _commentDiaryDates.value = commentDiary.toList()
        _aloneDiaryDates.value = aloneDiary.toList()
        _monthDiaries.value = list
    }

    //for diary data
    fun setSelectedDiary(diary: Diary?) {
        if (diary == null) {
            _selectedDiary.value = null
            _commentList.value = emptyList()
        } else {
//            if(diary.date == ymdFormat(getCodaToday())){
//                viewModelScope.launch {
//                    deleteDiary()
//                }
//            }
            _selectedDiary.value = diary
            _commentList.value = diary.commentList
        }
    }


    fun setDeliveryYN(type: Char) {
//        _selectedDiary.value!!.deliveryYN = type
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

    fun deleteLocalReportedComment(commentId: Long) {
        //신고/차단 코멘트 삭제
        val newDiary = Diary(
            selectedDiary.value!!.id,
            selectedDiary.value!!.title,
            selectedDiary.value!!.content,
            selectedDiary.value!!.date,
            selectedDiary.value!!.deliveryYN,
            selectedDiary.value!!.commentList
        )
        for (idx in _selectedDiary.value!!.commentList.indices) {
            val comment = selectedDiary.value!!.commentList[idx]
            if (comment.id == commentId) {
                newDiary.commentList.removeAt(idx)
                setSelectedDiary(newDiary)
                return
            }
        }
    }

    private fun setCanSendCommentDiary() {
        _canSendCommentDiary.value =
            if (diaryHead.value == null || diaryContent.value == null) {
                false
            } else {
                diaryHead.value!!.isNotBlank() && diaryContent.value!!.length >= 100
            }
    }

    fun changeSelectDiaryTypeToolbarIsExpanded() {
        _selectDiaryTypeToolbarIsExpanded.value = _selectDiaryTypeToolbarIsExpanded.value!! xor true
    }

    fun closeSelectDiaryTypeToolbarIsExpanded() {
        _selectDiaryTypeToolbarIsExpanded.value = false
    }

    fun likeLocalComment(commentId: Long) {
        val newDiary = Diary(
            selectedDiary.value!!.id,
            selectedDiary.value!!.title,
            selectedDiary.value!!.content,
            selectedDiary.value!!.date,
            selectedDiary.value!!.deliveryYN,
            selectedDiary.value!!.commentList
        )
        //신고한 코멘트 삭제
        for (idx in _selectedDiary.value!!.commentList.indices) {
            val comment = selectedDiary.value!!.commentList[idx]
            if (comment.id == commentId) {
                //like -> dislike, dislike -> like
                val newComment = Comment(
                    comment.id,
                    comment.content,
                    comment.date,
                    !comment.like
                )
                newDiary.commentList[idx] = newComment
//                _selectedDiary.value!!.commentList!![idx].like=true
                setSelectedDiary(newDiary)
                return
            }
        }
    }

    fun getMonthDiary(date: String) = viewModelScope.launch {
        onLoading()
        with(getMonthDiaryUseCase(date)) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    Timber.e("여기 몇개 ${data.size}")
                    setMonthDiaries(data)
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
    }

    fun handleAloneDiary() {
        if (aloneDiary.value == null) return
        when (val request = aloneDiary.value) {
            //id 없는 경우 저장
            is SaveDiaryModel -> {
                saveAloneDiary(request)
            }
            is EditDiaryModel -> {
                //글자 없는 경우 삭제
                if (request.content.isBlank() && request.title.isBlank()) {
                    deleteAloneDiary()
                } else {
                    //글자 있는 경우 수정
                    editAloneDiary(request)
                }
            }
        }
    }

    private fun saveAloneDiary(request: SaveDiaryModel) = viewModelScope.launch {
        with(saveDiaryUseCase(request)) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    val newDiary = Diary(
                        this.data.id,
                        request.title,
                        request.content,
                        request.date,
                        request.deliveryYN,
                        emptyList<Comment>().toMutableList()
                    )
                    setSelectedDiary(newDiary)
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
    }

    private fun editAloneDiary(request: EditDiaryModel) = viewModelScope.launch {
        selectedDiary.value?.let { diary ->
            if (diary.content == request.content && diary.title == request.title) return@launch
            with(editDiaryUseCase(diary.id, request)) {
                offLoading()
                when (this) {
                    is UiState.Success -> {
                        val newDiary = Diary(
                            diary.id,
                            request.title,
                            request.content,
                            diary.date,
                            diary.deliveryYN,
                            diary.commentList
                        )
                        setSelectedDiary(newDiary)
                    }
                    is UiState.Error -> {
                        setMessage(message)
                    }
                    is UiState.Fail -> {
                        setMessage(message)
                    }
                }
            }
        }
    }

    private fun deleteAloneDiary() = viewModelScope.launch {
        if (selectedDiary.value?.id != null) {
            with(deleteDiaryUseCase(selectedDiary.value!!.id)) {
                offLoading()
                when (this) {
                    is UiState.Success -> {
                        setSelectedDiary(null)
                    }
                    is UiState.Error -> {
                        setMessage(message)
                    }
                    is UiState.Fail -> {
                        setMessage(message)
                    }
                }
            }
        }
    }


//    suspend fun saveDiary(deliveryYN: Char) = viewModelScope.async {
//        var isSuccess = false
//        onLoading()
//        val saveDiaryRequest = SaveDiaryModel(
//            title = diaryHead.value ?: "",
//            content = diaryContent.value ?: "",
//            date = selectedDate.value ?: "",
//            deliveryYN = deliveryYN,
//        )
//        with(saveDiaryUseCase(saveDiaryRequest)) {
//            offLoading()
//            when (this) {
//                is UiState.Success -> {
//                    val newDiary = Diary(
//                        this.data.id,
//                        saveDiaryRequest.title,
//                        saveDiaryRequest.content,
//                        saveDiaryRequest.date,
//                        saveDiaryRequest.deliveryYN,
//                        emptyList<Comment>().toMutableList()
//                    )
//                    setSelectedDiary(newDiary)
//                    setMessage("일기가 저장되었습니다.")
//                    isSuccess = true
//                }
//                is UiState.Error -> {
//                    setMessage(message)
//                }
//                is UiState.Fail -> {
//                    setMessage(message)
//                }
//            }
//        }
//        return@async isSuccess
//    }.await()

//    fun editDiary(diaryId: Long, editDiaryRequest: EditDiaryModel) =
//        viewModelScope.launch {
//            onLoading()
//            with(editDiaryUseCase(diaryId, editDiaryRequest)) {
//                offLoading()
//                when (this) {
//                    is UiState.Success -> {
//                        val newDiary = Diary(
//                            selectedDiary.value!!.id,
//                            editDiaryRequest.title,
//                            editDiaryRequest.content,
//                            selectedDiary.value!!.date,
//                            selectedDiary.value!!.deliveryYN,
//                            selectedDiary.value!!.commentList
//                        )
//                        setSelectedDiary(newDiary)
//                        setMessage("일기가 수정되었습니다.")
//                    }
//                    is UiState.Error -> {
//                        setMessage(message)
//                    }
//                    is UiState.Fail -> {
//                        setMessage(message)
//                    }
//                }
//            }
//        }
//
//    suspend fun deleteDiary() = viewModelScope.async {
//        var isSuccess = false
//        onLoading()
//        if (selectedDiary.value?.id != null) {
//            with(deleteDiaryUseCase(selectedDiary.value!!.id)) {
//                offLoading()
//                when (this) {
//                    is UiState.Success -> {
//                        setMessage("일기가 삭제되었습니다.")
//                        isSuccess = true
//                    }
//                    is UiState.Error -> {
//                        setMessage(message)
//                    }
//                    is UiState.Fail -> {
//                        setMessage(message)
//                    }
//                }
//            }
//        }
//        return@async isSuccess
//    }.await()

    fun getDayWrittenComment(date: String) = viewModelScope.launch {
        onLoading()
        with(getPeriodCommentUseCase(date)) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    _haveDayWrittenComment.value = data.isEmpty()
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
    }

}