package com.movingmaker.presentation.viewmodel.mydiary

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.usecase.DeleteDiaryUseCase
import com.movingmaker.domain.usecase.DeleteTempDiaryUseCase
import com.movingmaker.domain.usecase.EditDiaryUseCase
import com.movingmaker.domain.usecase.GetPeriodCommentsUseCase
import com.movingmaker.domain.usecase.GetPeriodDiariesFlowUseCase
import com.movingmaker.domain.usecase.SaveDiaryUseCase
import com.movingmaker.domain.usecase.SaveTempDiaryUseCase
import com.movingmaker.domain.usecase.UpdatePeriodDiariesUseCase
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseViewModel
import com.movingmaker.presentation.util.DIARY_CONTENT_MINIMUM_LENGTH
import com.movingmaker.presentation.util.DIARY_TYPE
import com.movingmaker.presentation.util.EMPTY_USER
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymFormatForLocalDate
import com.movingmaker.presentation.util.ymFormatForString
import com.movingmaker.presentation.util.ymdFormat
import com.movingmaker.presentation.util.ymdToCalendarDay
import com.movingmaker.presentation.util.ymdToDate
import com.movingmaker.presentation.view.main.mydiary.DiaryState
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

//todo edit, delete 등 viewmodel 분리하기
@HiltViewModel
class MyDiaryViewModel @Inject constructor(
    private val saveDiaryUseCase: SaveDiaryUseCase,
    private val editDiaryUseCase: EditDiaryUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val getPeriodCommentsUseCase: GetPeriodCommentsUseCase,
    private val updatePeriodDiariesUseCase: UpdatePeriodDiariesUseCase,
    private val saveTempDiaryUseCase: SaveTempDiaryUseCase,
    getPeriodDiariesFlowUseCase: GetPeriodDiariesFlowUseCase,
    private val deleteTempDiaryUseCase: DeleteTempDiaryUseCase
) : BaseViewModel() {

    private var userId = EMPTY_USER

//    private val localDiaries = MutableStateFlow<List<Diary>>(emptyList())
//    private val monthDiaries = getPeriodDiariesFlowUseCase("")

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

    private var _selectedDiary = MutableStateFlow<Diary?>(null)
    val selectedDiary: StateFlow<Diary?>
        get() = _selectedDiary

    private var _haveDayWrittenComment =
        MutableStateFlow<Boolean>(false)
    val haveDayWrittenComment: StateFlow<Boolean>
        get() = _haveDayWrittenComment

    private var _selectedDate = MutableStateFlow<String?>(ymdFormat(getCodaToday())!!)
    val selectedDate: StateFlow<String?>
        get() = _selectedDate

    private var _selectedYearMonth = MutableLiveData<String>().apply {
        value = ymFormatForLocalDate(getCodaToday())
    }
    val selectedYearMonth: LiveData<String>
        get() = _selectedYearMonth

    @ExperimentalCoroutinesApi
    private val localDiaries = selectedYearMonth.asFlow().filterNotNull().flatMapLatest {
        getPeriodDiariesFlowUseCase(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )


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
        updatePeriodDiaries(ymFormatForLocalDate(getCodaToday())!!)
    }

    private var cachedLocalDiaries: List<Diary> = emptyList()
    @ExperimentalCoroutinesApi
    val selectedDateWithMonthDiaries = selectedDate.combine(localDiaries) { date, diaries ->
        //date만 변경되었거나 일기가 없는 경우 화면 갱신 필요 x
        if (diaries == cachedLocalDiaries || diaries.isEmpty()) {
            Pair(date, null)
        } else { // date + month 모두 변경된 경우
            setMonthDiaries(diaries)
            cachedLocalDiaries = diaries
            Pair(date, diaries)
        }
    }

    //todo activity에서 collect하기 고민
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val diaryState: StateFlow<DiaryState> = combine(
        selectedDate, selectedDiary, _haveDayWrittenComment
    ) { ymdDate, diary, haveWrittenComment ->
        Timber.e("여기 diaryState combine date: ${ymdDate} diary: ${diary} writtenCOmeent: ${haveWrittenComment}")
        if (ymdDate == null) {
            //선택한 날짜 없는 경우
            DiaryState.NoneSelectedDiary
        } else {
            val date = ymdToDate(ymdDate)
            if (date == null) {
                //선택한 날짜 없는 경우
                DiaryState.NoneSelectedDiary
            } else if (date > getCodaToday()) {
                //미래 날짜 선택한 경우
                DiaryState.SelectedFutureDiary
            } else if (date == getCodaToday()) { //오늘 날짜 선택한 경우
                when {
                    //일기 없는 경우
                    diary == null -> {
                        DiaryState.EmptyDiary(ymdDate)
                    }
                    //혼자 일기인 경우
                    diary.deliveryYN == 'N' -> {
                        DiaryState.AloneDiary(diary)
                    }
                    // 코멘트 일기인 경우
                    //임시 저장인 경우
                    diary.userId == userId -> {
                        DiaryState.CommentDiary.TempDiaryInTime(diary)
                    }
                    else -> { //임시 저장 아닌 경우
                        DiaryState.CommentDiary.HaveNotCommentInTime(diary)
                    }
                }
            } else {
                //과거 날짜 선택한 경우
                when {
                    //일기 없는 경우
                    diary == null -> {
                        DiaryState.EmptyDiary(ymdDate)
                    }
                    //혼자 일기인 경우
                    diary.deliveryYN == 'N' -> {
                        DiaryState.AloneDiary(diary)
                    }
                    //임시 저장인 경우
                    diary.userId == userId -> {
                        DiaryState.CommentDiary.TempDiaryOutTime(diary)
                    }
                    else -> { //임시 저장 아닌 경우
                        when (date) { //하루 지난 일기인 경우
                            getCodaToday().minusDays(1) -> {
                                when {
                                    //코멘트 없는 경우
                                    diary.commentList.isEmpty() -> {
                                        DiaryState.CommentDiary.HaveNotCommentInTime(diary)
                                    }
                                    //코멘트 있고 열 수 있는 경우
                                    haveWrittenComment -> {
                                        DiaryState.CommentDiary.HaveCommentInTimeCanOpen(diary)
                                    }
                                    //코멘트 있는데 못 여는 경우
                                    else -> {
                                        DiaryState.CommentDiary.HaveCommentInTimeCannotOpen(diary)
                                    }
                                }
                            }
                            else -> { // 이틀 이상 지난 일기인 경우
                                when {
                                    diary.commentList.isEmpty() -> {
                                        DiaryState.CommentDiary.HaveNotCommentOutTime(diary)
                                    }
                                    haveWrittenComment -> {
                                        DiaryState.CommentDiary.HaveCommentOutTimeCanOpen(diary)
                                    }
                                    else -> {
                                        DiaryState.CommentDiary.HaveCommentOutTimeCannotOpen(diary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }.filter {//본인 or 서버 저장된 일기 아닌경우 filter
        (it is DiaryState.CommentDiary && it.diary.userId != -1L && it.diary.userId != userId).not()
//        .debounce(100L)
    }.mapLatest {
        it
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        DiaryState.Init
    )

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

    private fun setMonthDiaries(list: List<Diary>) {
        val aloneDiary = ArrayList<CalendarDay>()
        val commentDiary = ArrayList<CalendarDay>()
        val tempDiary = ArrayList<CalendarDay>()
        for (selectedDiary in list) {
            ymdToCalendarDay(selectedDiary.date)?.let { date ->
                when (selectedDiary.deliveryYN) {
                    'Y' -> {
                        if (selectedDiary.userId == userId) {
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
        _selectedDiary.value = diary
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

    fun setUserId(id: Long) {
        userId = id
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

    suspend fun getDiaryInMonth(selectedDateYMD: String): Diary? =
        withContext(Dispatchers.Default) {
            monthDiaries.value?.let { monthDiaries ->
                monthDiaries.find { it.date == selectedDateYMD }
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

    fun updatePeriodDiaries(date: String) = viewModelScope.launch {
        onLoading()
        with(updatePeriodDiariesUseCase(date)) {
            offLoading()
            when (this) {
                is UiState.Success -> {/*no-op*/
                    Timber.e("영진 remote 일기 ${this.data}")
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

    fun handleDiary(selectedDiaryType: DIARY_TYPE?) {
        viewModelScope.launch {
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
        onLoading()
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
            val date = diary.date.ymFormatForString() ?: ""
            with(editDiaryUseCase(diary.id, date, request)) {
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
        offLoading()
    }

    private fun deleteAloneDiary() = viewModelScope.launch {
        selectedDiary.value?.let { diary ->
            val date = diary.date.ymFormatForString() ?: ""
            with(deleteDiaryUseCase(diary.id, date)) {
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
        offLoading()
    }

    suspend fun sendCommentDiary() = withContext(viewModelScope.coroutineContext) {
        //서버에 저장하는 코멘트 다이어리는 userId 필요 x
        // 임시저장 일기인 경우 저장, 혼자 쓴 일기 -> 코멘트 일기인 경우 edit
        if (selectedDiary.value != null && diaryState.value !is DiaryState.CommentDiary.TempDiaryInTime) {
            editDiary(
                EditDiaryModel(
                    diaryHead.value!!,
                    diaryContent.value!!,
                    'Y'
                )
            )
        } else {
            //임시 저장 일기 삭제 후 리모트에 저장 -> 리모트 일기 로컬에 캐시
            //todo state Temp인 경우에만 deleteTemp
            deleteTempCommentDiary()
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
            val newDiary = Diary.buildTempDiary(
                userId,
                diaryHead.value!!,
                diaryContent.value!!,
                selectedDate.value!!
            )
            setSelectedDiary(newDiary)
            saveTempDiaryUseCase(
                newDiary
            )
        }
    }

    suspend fun deleteTempCommentDiary() = withContext(viewModelScope.coroutineContext) {
        onLoading()
        selectedDiary.value?.let { diary ->
            with(deleteTempDiaryUseCase(diary)) {
                when (this) {
                    //삭제된 행의 수
                    1 -> {
                        if (diaryState.value is DiaryState.CommentDiary.TempDiaryOutTime) {
                            setMessage("일기가 삭제되었습니다.")
                        }
                        setSelectedDiary(null)
                    }
                    else -> {
                        setMessage("오류가 발생했습니다.")
                    }
                }
            }
        }
        offLoading()
    }

    suspend fun deleteDiary() = viewModelScope.async {
        var isSuccess = false
        onLoading()
        selectedDiary.value?.let { diary ->
            val date = diary.date.ymFormatForString() ?: ""
            with(deleteDiaryUseCase(diary.id, date)) {
                offLoading()
                when (this) {
                    is UiState.Success -> {
                        setSelectedDiary(null)
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
        offLoading()
        return@async isSuccess
    }.await()

    fun getDayWrittenComment(date: String?) = viewModelScope.launch {
        if (date == null) {
            _haveDayWrittenComment.value = false
            return@launch
        }
        onLoading()
        with(getPeriodCommentsUseCase(date)) {
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