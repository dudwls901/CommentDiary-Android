package com.movingmaker.presentation.viewmodel.mydiary

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.usecase.DeleteDiaryUseCase
import com.movingmaker.domain.usecase.DeleteTempCommentDiaryUseCase
import com.movingmaker.domain.usecase.EditDiaryUseCase
import com.movingmaker.domain.usecase.GetMonthDiaryUseCase
import com.movingmaker.domain.usecase.GetPeriodCommentUseCase
import com.movingmaker.domain.usecase.GetRemoteMonthDiaryUseCase
import com.movingmaker.domain.usecase.SaveDiaryUseCase
import com.movingmaker.domain.usecase.SaveTempDiaryUseCase
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseViewModel
import com.movingmaker.presentation.util.DIARY_CONTENT_MINIMUM_LENGTH
import com.movingmaker.presentation.util.DIARY_TYPE
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymFormatForLocalDate
import com.movingmaker.presentation.util.ymFormatForString
import com.movingmaker.presentation.util.ymdFormat
import com.movingmaker.presentation.util.ymdToCalendarDay
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyDiaryViewModel @Inject constructor(
    private val saveDiaryUseCase: SaveDiaryUseCase,
    private val editDiaryUseCase: EditDiaryUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    getMonthDiaryUseCase: GetMonthDiaryUseCase,
    private val getPeriodCommentUseCase: GetPeriodCommentUseCase,
    private val getRemoteMonthDiaryUseCase: GetRemoteMonthDiaryUseCase,
    private val saveTempDiaryUseCase: SaveTempDiaryUseCase,
    private val deleteTempCommentDiaryUseCase: DeleteTempCommentDiaryUseCase
) : BaseViewModel() {

    //    val monthDiaries: LiveData<List<Diary>?> =
//        getMonthDiaryUseCase(ymdFormat(getCodaToday())!!).asLiveData()
//    private val _selectedReview: MutableStateFlow<List<Diary>> = MutableStateFlow(listOf())
//    val selectedReview = _selectedReview.asStateFlow()
    val localDiaries = getMonthDiaryUseCase("null").asLiveData()

    private var _aloneDiaryDates = MutableLiveData<List<CalendarDay>>()
    val aloneDiaryDates: LiveData<List<CalendarDay>>
        get() = _aloneDiaryDates

    private var _commentDiaryDates = MutableLiveData<List<CalendarDay>>()
    val commentDiaryDates: LiveData<List<CalendarDay>>
        get() = _commentDiaryDates

    private var _tempDiaryDates = MutableLiveData<List<CalendarDay>>()
    val tempDiaryDates: LiveData<List<CalendarDay>>
        get() = _tempDiaryDates

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

    val canSendCommentDiary = MediatorLiveData<Boolean>().apply {
        addSource(diaryHead) { diaryHead ->
            value =
                diaryHead.isNullOrBlank().not()
                        && diaryContent.value.isNullOrBlank().not()
                        && diaryContent.value!!.length > DIARY_CONTENT_MINIMUM_LENGTH
        }
        addSource(diaryContent) { diaryContent ->
            value =
                diaryHead.value.isNullOrBlank().not()
                        && diaryContent.isNullOrBlank().not()
                        && diaryContent.length > DIARY_CONTENT_MINIMUM_LENGTH
        }
    }

    init {
        getRemoteCommentDiaries(ymFormatForLocalDate(getCodaToday())!!)
    }

    /**
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
            if (diaryType == DIARY_TYPE.ALONE_DIARY) {
                combineDiaryHead(this)
                combineDiaryContent(this)
            } else {
                value = null
            }
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

    private fun combineDiaryContent(aloneDiary: MediatorLiveData<Any>) = with(aloneDiary) {
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
            'N'
        )
    }

    fun setMonthDiaries(list: List<Diary>) {
        val aloneDiary = ArrayList<CalendarDay>()
        val commentDiary = ArrayList<CalendarDay>()
        val tempDiary = ArrayList<CalendarDay>()
        for (selectedDiary in list) {
            ymdToCalendarDay(selectedDiary.date)?.let { date ->
                when (selectedDiary.deliveryYN) {
                    'Y' -> {
                        if (selectedDiary.userId == -1L) {
                            tempDiary.add(date)
                        } else {
                            commentDiary.add(date)
                        }
                    }
                    else -> {
                        aloneDiary.add(date)
                    }
                }
            }
        }

        _commentDiaryDates.value = commentDiary.toList()
        _aloneDiaryDates.value = aloneDiary.toList()
        _tempDiaryDates.value = tempDiary.toList()
        _monthDiaries.value = list
    }

    //for diary data
    fun setSelectedDiary(diary: Diary?) {
        if (diary == null) {
            _selectedDiary.value = null
            _commentList.value = emptyList()
        } else {
            _selectedDiary.value = diary
            _commentList.value = diary.commentList
        }
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
            id = selectedDiary.value!!.id,
            title = selectedDiary.value!!.title,
            content = selectedDiary.value!!.content,
            date = selectedDiary.value!!.date,
            deliveryYN = selectedDiary.value!!.deliveryYN,
            commentList = selectedDiary.value!!.commentList
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

    fun changeSelectDiaryTypeToolbarIsExpanded() {
        _selectDiaryTypeToolbarIsExpanded.value = _selectDiaryTypeToolbarIsExpanded.value!! xor true
    }

    fun closeSelectDiaryTypeToolbarIsExpanded() {
        _selectDiaryTypeToolbarIsExpanded.value = false
    }

    fun likeLocalComment(commentId: Long) {
        val newDiary = Diary(
            id = selectedDiary.value!!.id,
            title = selectedDiary.value!!.title,
            content = selectedDiary.value!!.content,
            date = selectedDiary.value!!.date,
            deliveryYN = selectedDiary.value!!.deliveryYN,
            commentList = selectedDiary.value!!.commentList
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

    fun getRemoteCommentDiaries(date: String) = viewModelScope.launch {
        onLoading()
        with(getRemoteMonthDiaryUseCase(date)) {
            offLoading()
            when (this) {
                is UiState.Success -> {
//                    setMonthDiaries(data)
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

    suspend fun handleDiary(selectedDiaryType: DIARY_TYPE?) {
        when (selectedDiaryType) {
            DIARY_TYPE.ALONE_DIARY -> {
                handleAloneDiary()
            }
            DIARY_TYPE.COMMENT_DIARY -> {
                handleCommentDiary()
            }
            else -> { /*no op*/
            }
        }
    }

    private suspend fun handleCommentDiary() {
        //비어있으면 삭제
        if (diaryHead.value!!.isBlank() && diaryContent.value!!.isBlank()) {
            deleteTempCommentDiary()
        } else {//한 글자라도 있으면 임시 저장
            saveTempCommentDiary()
        }
    }

    private fun handleAloneDiary() {
        if (aloneDiary.value == null) return
        when (val request = aloneDiary.value) {
            //id 없는 경우 저장
            is SaveDiaryModel -> {
                //글자 없는 경우 저장 x
                if (request.content.isBlank() && request.title.isBlank()) {
                    return
                }
                saveDiary(request)
            }
            is EditDiaryModel -> {
                //글자 없는 경우 삭제
                if (request.content.isBlank() && request.title.isBlank()) {
                    deleteAloneDiary()
                } else {
                    //글자 있는 경우 수정
                    editDiary(request)
                }
            }
        }
    }

    private fun saveDiary(request: SaveDiaryModel) = viewModelScope.launch {
        with(saveDiaryUseCase(request)) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    val newDiary = Diary(
                        id = this.data.id,
                        title = request.title,
                        content = request.content,
                        date = request.date,
                        deliveryYN = request.deliveryYN,
                        commentList = emptyList<Comment>().toMutableList()
                    )
                    request.date.ymFormatForString()?.let {
                        getRemoteCommentDiaries(it)
                    }
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

    private fun editDiary(request: EditDiaryModel) = viewModelScope.launch {
        selectedDiary.value?.let { diary ->
            if (diary.content == request.content && diary.title == request.title) return@launch
            with(editDiaryUseCase(diary.id, request)) {
                offLoading()
                when (this) {
                    is UiState.Success -> {
                        val newDiary = Diary(
                            id = diary.id,
                            title = request.title,
                            content = request.content,
                            date = diary.date,
                            deliveryYN = request.deliveryYN,
                            commentList = diary.commentList
                        )
                        diary.date.ymFormatForString()?.let {
                            getRemoteCommentDiaries(it)
                        }
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
        selectedDiary.value?.let { diary ->
            with(deleteDiaryUseCase(diary.id)) {
                offLoading()
                when (this) {
                    is UiState.Success -> {
                        diary.date.ymFormatForString()?.let {
                            getRemoteCommentDiaries(it)
                        }
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

    suspend fun sendCommentDiary() = withContext(viewModelScope.coroutineContext) {
        //서버에 저장하는 코멘트 다이어리는 userId 필요 x
        if (selectedDiary.value != null) {
            editDiary(
                EditDiaryModel(
                    diaryHead.value!!,
                    diaryContent.value!!,
                    'Y'
                )
            )
        } else {
            saveDiary(
                SaveDiaryModel(
                    diaryHead.value!!,
                    diaryContent.value!!,
                    selectedDate.value!!,
                    'Y'
                )
            )
        }
    }

    /**
     * head, content 한 글자라도 있는 경우 임시 저장
     * */
    private suspend fun saveTempCommentDiary() {
        if (diaryHead.value!!.isNotBlank() || diaryContent.value!!.isNotBlank()) {
            saveTempDiaryUseCase(
                Diary(
                    id = -100,
                    userId = 30,
                    title = diaryHead.value!!,
                    content = diaryContent.value!!,
                    date = selectedDate.value!!,
                    deliveryYN = 'Y',
                    commentList = emptyList<Comment>().toMutableList()
                )
            )
        }
    }

    private suspend fun deleteTempCommentDiary() = withContext(viewModelScope.coroutineContext) {
        onLoading()
        if (selectedDiary.value != null) {
            with(deleteTempCommentDiaryUseCase(selectedDiary.value!!)) {
                when (this) {
                    1 -> {
                        setMessage("일기가 삭제되었습니다.")
                    }
                    else -> {
                        setMessage("오류가 발생했습니다.")
                    }
                }
            }
        } else {
            offLoading()
        }
    }

    suspend fun deleteDiary() = viewModelScope.async {
        var isSuccess = false
        onLoading()
        if (selectedDiary.value?.id != null) {
            with(deleteDiaryUseCase(selectedDiary.value!!.id)) {
                offLoading()
                when (this) {
                    is UiState.Success -> {
                        setMessage("일기가 삭제되었습니다.")
                        isSuccess = true
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
        return@async isSuccess
    }.await()

    fun getDayWrittenComment(date: String) = viewModelScope.launch {
        onLoading()
        with(getPeriodCommentUseCase(date)) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    _haveDayWrittenComment.value = data.isNotEmpty()
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