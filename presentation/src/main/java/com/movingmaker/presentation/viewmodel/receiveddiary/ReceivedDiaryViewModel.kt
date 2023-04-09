package com.movingmaker.presentation.viewmodel.receiveddiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.ReportDiaryModel
import com.movingmaker.domain.model.request.SaveCommentModel
import com.movingmaker.domain.model.response.ReceivedDiary
import com.movingmaker.domain.usecase.GetReceivedDiaryUseCase
import com.movingmaker.domain.usecase.ReportDiaryUseCase
import com.movingmaker.domain.usecase.SaveCommentUseCase
import com.movingmaker.presentation.base.BaseViewModel
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymdFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReceivedDiaryViewModel @Inject constructor(
    private val receivedDiaryUseCase: GetReceivedDiaryUseCase,
    private val saveCommentUseCase: SaveCommentUseCase,
    private val reportDiaryUseCase: ReportDiaryUseCase,
) : BaseViewModel() {

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
        val date = ymdFormat(getCodaToday())!!
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
        val date = ymdFormat(getCodaToday())!!
        with(
            saveCommentUseCase(
                SaveCommentModel(
                    id = receivedDiary.value!!.id,
                    date = date,
                    content = content
                )
            )
        ) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    setMessage("코멘트가 전송되었습니다.")
                    getReceiveDiary()
                }
                is UiState.Error -> {
//                    setReceivedDiary(null)
                    setMessage(message)
                }
                is UiState.Fail -> {
//                    setReceivedDiary(null)
                    setMessage(message)
                }
            }
        }
    }

    //일기 신고
    fun setResponseReportDiary(content: String) = viewModelScope.launch {
        onLoading()
        with(
            reportDiaryUseCase(
                ReportDiaryModel(
                    id = receivedDiary.value!!.id,
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
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
    }
}