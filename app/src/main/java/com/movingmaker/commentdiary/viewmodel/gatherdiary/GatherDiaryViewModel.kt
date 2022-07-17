package com.movingmaker.commentdiary.viewmodel.gatherdiary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.*
import com.movingmaker.commentdiary.data.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.repository.GatherDiaryRepository
import com.movingmaker.commentdiary.util.DateConverter
import kotlinx.coroutines.*
import retrofit2.Response

class GatherDiaryViewModel : ViewModel() {

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

    private var _handleComment = MutableLiveData<Pair<Long,String>>()
        val handleComment: LiveData<Pair<Long,String>>
            get() = _handleComment

    private var _diaryList = MutableLiveData<List<Diary>>()
    private var _selectedMonth = MutableLiveData<String>()
    private var _responseLikeComment = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseReportComment = MutableLiveData<Response<IsSuccessResponse>>()


    val diaryList: LiveData<List<Diary>>
        get() = _diaryList

    val selectedMonth: LiveData<String>
        get() = _selectedMonth

    val responseLikeComment: LiveData<Response<IsSuccessResponse>>
        get() = _responseLikeComment

    val responseReportComment: LiveData<Response<IsSuccessResponse>>
        get() = _responseReportComment

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
                GatherDiaryRepository.INSTANCE.getAllDiary()
            else
                GatherDiaryRepository.INSTANCE.getMonthDiary(date)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                //todo 그냥 리스폰스에서 주는 200 말고 우리 서버가 직접 내려주는 1000사용
                Log.d("code확인", "setResponseGetDiaryList: ${it.code()} ${response!!.body()!!.code}")
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
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            onError("다시 로그인해 주세요.")
                            CodaApplication.getInstance().logOut()
                        } else {
                            _snackMessage.postValue("일기를 불러오지 못했습니다.")
//                            onError(it.message)
                        }
                    }
                }
            }
        }
    }

    fun setResponseReportComment(reportCommentRequest: ReportCommentRequest) = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<IsSuccessResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = GatherDiaryRepository.INSTANCE.reportComment(reportCommentRequest)
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
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        if (it.status == 401) {
                            onError("다시 로그인해 주세요.")
                            CodaApplication.getInstance().logOut()
                        } else {
                            onError(it.message)
                        }
                    }
                }
            }
        }
    }

    fun setResponseLikeComment(commentId: Long) = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<IsSuccessResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = GatherDiaryRepository.INSTANCE.likeComment(commentId)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    Log.d("-->", "setResponseLikeComment: ${result}")
                    when (it.code()) {
                        200 -> {
                            _handleComment.postValue(Pair(commentId, "like"))
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
                            onError(it.message)
                        }
                    }
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