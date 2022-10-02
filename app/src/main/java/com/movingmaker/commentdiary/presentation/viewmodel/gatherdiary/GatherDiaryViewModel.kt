package com.movingmaker.commentdiary.presentation.viewmodel.gatherdiary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.common.util.DateConverter
import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.data.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.domain.usecase.GetAllDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.GetMonthDiaryUseCase
import com.movingmaker.commentdiary.domain.usecase.LikeCommentUseCase
import com.movingmaker.commentdiary.domain.usecase.ReportCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GatherDiaryViewModel @Inject constructor(
    private val getAllDiaryUseCase: GetAllDiaryUseCase,
    private val getMonthDiaryUseCase: GetMonthDiaryUseCase,
    private val reportCommentUseCase: ReportCommentUseCase,
    private val likeCommentUseCase: LikeCommentUseCase
) : ViewModel() {

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

    fun setDiaryList(list: List<Diary>) {
        _diaryList.value = list
    }

    fun setSelectedMonth(date: String) {
        _selectedMonth.value = date
    }

    fun setResponseGetDiaryList(date: String) = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<DiaryListResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = if (date == "all")
                getAllDiaryUseCase()
            else
                getMonthDiaryUseCase(date)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                Timber.d("setResponseGetDiaryList: ${it.code()} ${response!!.body()!!.code}")
                it.body()?.let { result ->
                    when (it.code()) {
                        200 -> {
                            setDiaryList(result.result)
                        }
                        else -> onError(it.message())
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
//                    RetrofitClient.getErrorResponse(errorBody)?.let {
//                        if (it.status == 401) {
//                            onError("다시 로그인해 주세요.")
//                            CodaApplication.getInstance().logOut()
//                        } else {
//                            _snackMessage.postValue("일기를 불러오지 못했습니다.")
////                            onError(it.message)
//                        }
//                    }
                }
            }
        }
    }

    fun setResponseReportComment(reportCommentRequest: ReportCommentRequest) =
        viewModelScope.launch {
            _loading.postValue(true)
            var response: Response<IsSuccessResponse>? = null
            val job = launch(Dispatchers.Main + exceptionHandler) {
                response = reportCommentUseCase(reportCommentRequest)
            }
            job.join()
            _loading.postValue(false)
            response?.let {
                if (it.isSuccessful) {
                    it.body()?.let { result ->
                        when (it.code()) {
                            200 -> {
                                _handleComment.postValue(Pair(reportCommentRequest.id, "report"))
                            }
                            else -> onError(it.message())
                        }
                    }
                } else {
                    it.errorBody()?.let { errorBody ->
//                        RetrofitClient.getErrorResponse(errorBody)?.let {
//                            if (it.status == 401) {
//                                onError("다시 로그인해 주세요.")
//                                CodaApplication.getInstance().logOut()
//                            } else {
//                                onError(it.message)
//                            }
//                        }
                    }
                }
            }
        }

    fun setResponseLikeComment(commentId: Long) = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<IsSuccessResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = likeCommentUseCase(commentId)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    Timber.d("setResponseLikeComment: ${result}")
                    when (it.code()) {
                        200 -> {
                            _handleComment.postValue(Pair(commentId, "like"))
                        }
                        else -> onError(it.message())
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
//                    RetrofitClient.getErrorResponse(errorBody)?.let {
//                        if (it.status == 401) {
//                            onError("다시 로그인해 주세요.")
//                            CodaApplication.getInstance().logOut()
//                        } else {
//                            onError(it.message)
//                        }
//                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}