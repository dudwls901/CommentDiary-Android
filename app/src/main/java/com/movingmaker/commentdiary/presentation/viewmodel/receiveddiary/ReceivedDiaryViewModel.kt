package com.movingmaker.commentdiary.presentation.viewmodel.receiveddiary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.common.CodaApplication
import com.movingmaker.commentdiary.data.model.ReceivedDiary
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.*
import com.movingmaker.commentdiary.data.remote.response.*
import com.movingmaker.commentdiary.data.repository.ReceivedDiaryRepository
import com.movingmaker.commentdiary.common.util.DateConverter
import kotlinx.coroutines.*
import retrofit2.Response

class ReceivedDiaryViewModel : ViewModel() {

    var mJob: Job? = null

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private var _snackMessage = MutableLiveData<String>()
    val snackMessage: LiveData<String>
        get() = _snackMessage

    //코루틴 예외처리 핸들러
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private var _receivedDiary = MutableLiveData<ReceivedDiary?>()
    val receivedDiary: LiveData<ReceivedDiary?>
        get() = _receivedDiary

    private var _commentTextCount = MutableLiveData<Int>()
    val commentTextCount: LiveData<Int>
        get() = _commentTextCount

    init {
        _commentTextCount.value = 0
    }

    private fun setReceivedDiary(diary: ReceivedDiary?) {
        _receivedDiary.value = diary
    }

    fun setCommentTextCount(cnt: Int) {
        _commentTextCount.value = cnt
    }

    //오늘 날짜로 받은 일기 조회
    fun getReceiveDiary() = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<ReceivedDiaryResponse>? = null
        val date = DateConverter.ymdFormat(DateConverter.getCodaToday())!!
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = ReceivedDiaryRepository.INSTANCE.getReceivedDiary(date)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    when (it.code()) {
                        200 -> {
                            setReceivedDiary(it.body()!!.result)
                        }
                        else -> {
                            setReceivedDiary(null)
                            onError(it.message())
                        }
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        //Received 일기 없는 경우 화면에 텍스트로 (toast X)
                        if (it.status == 401) {
                            onError("다시 로그인해 주세요.")
                            CodaApplication.getInstance().logOut()
                        }
                        else{
                            setReceivedDiary(null)
                        }
                    }
                }
            }
        }
    }

    //코멘트 저장
    fun setResponseSaveComment(content: String) = viewModelScope.launch {

        _loading.postValue(true)
        val date = DateConverter.ymdFormat(DateConverter.getCodaToday())
        var response: Response<IsSuccessResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = ReceivedDiaryRepository.INSTANCE.saveComment(
                SaveCommentRequest(
                    id = receivedDiary.value!!.id!!,
                    date = date!!,
                    content = content
                )
            )
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    when (it.code()) {
                        200 -> {
                            _toastMessage.postValue("코멘트가 전송되었습니다.")
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
                        }
                        else {
                            onError(it.message)
                        }
                        Log.d("viewmodel", "observerDatas: $it")
                    }
                }
            }
        }
    }

    //일기 신고
    fun setResponseReportDiary(content: String) = viewModelScope.launch{

        _loading.postValue(true)
        var response: Response<IsSuccessResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = ReceivedDiaryRepository.INSTANCE.reportDiary(
                ReportDiaryRequest(
                    id = receivedDiary.value!!.id!!,
                    content = content
                )
            )
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { result ->
                    when (it.code()) {
                        200 -> {
                            getReceiveDiary()
                        }
                        else -> onError(it.message())
                    }
                }
            } else {
                it.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        onError(it.message)
                        if (it.status == 401) {
                            onError("다시 로그인해 주세요.")
                            CodaApplication.getInstance().logOut()
                        }
                        else{
                            onError(it.message)
                        }
                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false

    }

    override fun onCleared() {
        super.onCleared()
        mJob?.cancel()
    }

}