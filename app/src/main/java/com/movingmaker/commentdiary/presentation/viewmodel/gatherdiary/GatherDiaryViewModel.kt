package com.movingmaker.commentdiary.presentation.viewmodel.gatherdiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.usecase.GetAllDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.GetMonthDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.LikeCommentUseCase
import com.movingmaker.commentdiary.domain.usecase.ReportCommentUseCase
import com.movingmaker.commentdiary.presentation.base.BaseViewModel
import com.movingmaker.commentdiary.presentation.util.DateConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GatherDiaryViewModel @Inject constructor(
    private val getAllDiaryUseCase: GetAllDiaryUseCase,
    private val getMonthDiaryUseCase: GetMonthDiaryUseCase,
    private val reportCommentUseCase: ReportCommentUseCase,
    private val likeCommentUseCase: LikeCommentUseCase
) : BaseViewModel() {

    private var _handleComment = MutableLiveData<Pair<Long, String>>()
    val handleComment: LiveData<Pair<Long, String>>
        get() = _handleComment

    private var _diaryList = MutableLiveData<List<Diary>>()
    val diaryList: LiveData<List<Diary>>
        get() = _diaryList

    private var _selectedMonth = MutableLiveData<String>()
    val selectedMonth: LiveData<String>
        get() = _selectedMonth

    init {
        _diaryList.value = emptyList()
        _selectedMonth.value = DateConverter.ymFormat(DateConverter.getCodaToday())
    }

    private fun setDiaryList(list: List<Diary>) {
        _diaryList.value = list
    }

    fun setSelectedMonth(date: String) {
        _selectedMonth.value = date
    }

    fun getDiaries(date: String) = viewModelScope.launch {
        onLoading()
        val response = if (date == "all") {
            getAllDiaryUseCase()
        } else {
            getMonthDiaryUseCase(date)
        }
        offLoading()
        with(response) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    setDiaryList(data)
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

    fun reportComment(reportCommentRequest: ReportCommentRequest) =
        viewModelScope.launch {
            onLoading()
            with(reportCommentUseCase(reportCommentRequest)) {
                offLoading()
                Timber.d("result $this")
                when (this) {
                    is UiState.Success -> {
                        _handleComment.value = Pair(reportCommentRequest.id, "report")
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

    fun likeComment(commentId: Long) = viewModelScope.launch {
        onLoading()
        with(likeCommentUseCase(commentId)) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    _handleComment.value = Pair(commentId, "like")
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