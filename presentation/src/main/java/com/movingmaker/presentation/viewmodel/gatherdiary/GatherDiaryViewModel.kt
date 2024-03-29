package com.movingmaker.presentation.viewmodel.gatherdiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.usecase.GetAllDiariesFlowUseCase
import com.movingmaker.domain.usecase.GetPeriodDiariesFlowUseCase
import com.movingmaker.domain.usecase.LikeCommentUseCase
import com.movingmaker.domain.usecase.ReportCommentUseCase
import com.movingmaker.domain.usecase.UpdateAllDiariesUseCase
import com.movingmaker.domain.usecase.UpdatePeriodDiariesUseCase
import com.movingmaker.presentation.base.BaseViewModel
import com.movingmaker.presentation.util.getCodaToday
import com.movingmaker.presentation.util.ymFormatForLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GatherDiaryViewModel @Inject constructor(
    private val getAllDiariesFlowUseCase: GetAllDiariesFlowUseCase,
    private val getPeriodDiariesFlowUseCase: GetPeriodDiariesFlowUseCase,
    private val updateAllDiariesUseCase: UpdateAllDiariesUseCase,
    private val updatePeriodDiariesUseCase: UpdatePeriodDiariesUseCase,
    private val reportCommentUseCase: ReportCommentUseCase,
    private val likeCommentUseCase: LikeCommentUseCase
) : BaseViewModel() {

    private var _handleComment = MutableLiveData<Pair<Long, String>>()
    val handleComment: LiveData<Pair<Long, String>>
        get() = _handleComment

   private var _selectedMonth = MutableStateFlow<String>(ymFormatForLocalDate(getCodaToday())!!)
    val selectedMonth: StateFlow<String> = _selectedMonth.asStateFlow()

    @ExperimentalCoroutinesApi
    val diaries = selectedMonth.filterNotNull().flatMapLatest { period ->
        when(period){
            "all" ->{
                getAllDiariesFlowUseCase()
            }
            else ->{
                getPeriodDiariesFlowUseCase(period)
            }
        }
    }

    fun updateDiaries(period: String) = viewModelScope.launch{
        onLoading()
        val response = if (period == "all") {
            updateAllDiariesUseCase()
        } else {
            updatePeriodDiariesUseCase(period)
        }
        offLoading()
        with(response) {
            offLoading()
            when (this) {
                is UiState.Success -> {/*no-op*/
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

    fun setSelectedMonth(date: String) {
        _selectedMonth.value = date
    }

    fun reportComment(reportCommentRequest: ReportCommentModel) =
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