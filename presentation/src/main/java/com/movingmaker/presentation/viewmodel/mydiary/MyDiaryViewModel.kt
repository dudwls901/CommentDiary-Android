package com.movingmaker.presentation.viewmodel.mydiary

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.usecase.DeleteDiaryUseCase
import com.movingmaker.domain.usecase.EditDiaryUseCase
import com.movingmaker.domain.usecase.GetMonthCommentUseCase
import com.movingmaker.domain.usecase.GetMonthDiaryUseCase
import com.movingmaker.domain.usecase.SaveDiaryUseCase
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseViewModel
import com.movingmaker.presentation.util.DIARY_TYPE
import com.movingmaker.presentation.util.DateConverter
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyDiaryViewModel @Inject constructor(
    private val saveDiaryUseCase: SaveDiaryUseCase,
    private val editDiaryUseCase: EditDiaryUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val getMonthDiaryUseCase: GetMonthDiaryUseCase,
    private val getMonthCommentUseCase: GetMonthCommentUseCase
) : BaseViewModel() {

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
        value = DateConverter.ymFormatForLocalDate(DateConverter.getCodaToday())
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
        //신고한 코멘트 삭제
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
        onLoading()
        with(getMonthDiaryUseCase(date)) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
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

    suspend fun saveDiary(deliveryYN: Char) = viewModelScope.async {
        var isSuccess = false
        onLoading()
        val saveDiaryRequest = SaveDiaryModel(
            title = diaryHeadText.value ?: "",
            content = diaryContentText.value ?: "",
            date = selectedDate.value ?: "",
            deliveryYN = deliveryYN,
            tempYN = 'N',
        )
        with(saveDiaryUseCase(saveDiaryRequest)) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    val newDiary = Diary(
                        this.data.id,
                        saveDiaryRequest.title,
                        saveDiaryRequest.content,
                        saveDiaryRequest.date,
                        saveDiaryRequest.deliveryYN,
                        saveDiaryRequest.tempYN,
                        null
                    )
                    setSelectedDiary(newDiary)
                    setMessage("일기가 저장되었습니다.")
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
        return@async isSuccess
    }.await()

    fun editDiary(diaryId: Long, editDiaryRequest: EditDiaryModel) =
        viewModelScope.launch {
            onLoading()
            with(editDiaryUseCase(diaryId, editDiaryRequest)) {
                offLoading()
                when (this) {
                    is UiState.Success -> {
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
                        setMessage("일기가 수정되었습니다.")
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

    suspend fun deleteDiary() = viewModelScope.async {
        var isSuccess = false
        onLoading()
        if (selectedDiary.value?.id != null) {
            with(deleteDiaryUseCase(selectedDiary.value!!.id!!)) {
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

    fun setResponseGetDayComment(date: String) = viewModelScope.launch {
        onLoading()
        with(getMonthCommentUseCase(date)) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    setHaveDayMyComment(data.isEmpty())
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