package com.movingmaker.commentdiary.presentation.viewmodel.receiveddiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.common.util.DateConverter
import com.movingmaker.commentdiary.common.util.Event
import com.movingmaker.commentdiary.data.model.ReceivedDiary
import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.usecase.GetReceivedDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.ReportDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.SaveCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReceivedDiaryViewModel @Inject constructor(
    private val receivedDiaryUseCase: GetReceivedDiaryUseCase,
    private val saveCommentUseCase: SaveCommentUseCase,
    private val reportDiaryUseCase: ReportDiaryUseCase,
) : ViewModel() {

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _snackMessage = MutableLiveData<Event<String>>()
    val snackMessage: LiveData<Event<String>>
        get() = _snackMessage

    private var _receivedDiary = MutableLiveData<ReceivedDiary?>()
    val receivedDiary: LiveData<ReceivedDiary?>
        get() = _receivedDiary

    private var _commentTextCount = MutableLiveData<Int>().apply { value = 0 }
    val commentTextCount: LiveData<Int>
        get() = _commentTextCount

    private fun setReceivedDiary(diary: ReceivedDiary?) {
        _receivedDiary.value = diary
    }

    fun setCommentTextCount(cnt: Int) {
        _commentTextCount.value = cnt
    }

    //오늘 날짜로 받은 일기 조회
    fun getReceiveDiary() = viewModelScope.launch {
        onLoading()
        val date = DateConverter.ymdFormat(DateConverter.getCodaToday())!!
        with(receivedDiaryUseCase(date)) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    setReceivedDiary(data)
                }
                is UiState.Error -> {
                    setReceivedDiary(null)
                }
                is UiState.Fail -> {
                    setReceivedDiary(null)
                }
            }
        }
    }

    //코멘트 저장
    fun saveComment(content: String) = viewModelScope.launch {
        onLoading()
        val date = DateConverter.ymdFormat(DateConverter.getCodaToday())!!
        with(
            saveCommentUseCase(
                SaveCommentRequest(
                    id = receivedDiary.value!!.id!!,
                    date = date,
                    content = content
                )
            )
        ) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    _snackMessage.value = Event("코멘트가 전송되었습니다.")
                }
                is UiState.Error -> {
                    setReceivedDiary(null)
                    _snackMessage.value = Event(message)
                }
                is UiState.Fail -> {
                    setReceivedDiary(null)
                    _snackMessage.value = Event(message)
                }
            }
        }
    }

    //일기 신고
    fun setResponseReportDiary(content: String) = viewModelScope.launch {
        onLoading()
        with(
            reportDiaryUseCase(
                ReportDiaryRequest(
                    id = receivedDiary.value!!.id!!,
                    content = content
                )
            )
        ) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    getReceiveDiary()
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