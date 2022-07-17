package com.movingmaker.commentdiary.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.*
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.data.remote.response.*
import com.movingmaker.commentdiary.data.repository.LogOutRepository
import com.movingmaker.commentdiary.data.repository.MyPageRepository
import com.movingmaker.commentdiary.util.Constant
import com.movingmaker.commentdiary.util.Constant.SUCCESS_CODE
import com.movingmaker.commentdiary.util.DateConverter
import com.movingmaker.commentdiary.view.main.mydiary.CalendarWithDiaryFragment.Companion.TAG
import kotlinx.coroutines.*
import retrofit2.Response

class MyPageViewModel : ViewModel() {
//    class MyPageViewModel (application: Application) : AndroidViewModel(application) {

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

    private var _isPasswordChanged = MutableLiveData<Boolean>()
    val isPasswordChanged: LiveData<Boolean>
        get() = _isPasswordChanged


    private var _myAccount = MutableLiveData<String>()
    private var _temperature = MutableLiveData<Double>()
    private var _commentList = MutableLiveData<List<Comment>>()
    private var _selectedMonth = MutableLiveData<String>()
    private var _passwordCorrect = MutableLiveData<Boolean>()
    private var _passwordCheckCorrect = MutableLiveData<Boolean>()
    private var _canChangePassword = MutableLiveData<Boolean>()
    private var _pushYN = MutableLiveData<Char>()

    val myAccount: LiveData<String>
        get() = _myAccount

    val temperature: LiveData<Double>
        get() = _temperature

    private var _loginType = MutableLiveData<String>()
    val loginType: LiveData<String>
        get() = _loginType

    val commentList: LiveData<List<Comment>>
        get() = _commentList

    val selectedMonth: LiveData<String>
        get() = _selectedMonth

    val passwordCorrect: LiveData<Boolean>
        get() = _passwordCorrect

    val passwordCheckCorrect: LiveData<Boolean>
        get() = _passwordCheckCorrect

    val canChangePassword: LiveData<Boolean>
        get() = _canChangePassword

    val pushYN: LiveData<Char>
        get() = _pushYN


    init {
        _selectedMonth.value = DateConverter.ymFormat(DateConverter.getCodaToday())
        _passwordCorrect.value = true
        _passwordCheckCorrect.value = true
        _canChangePassword.value = false
    }

    private fun setMyAccount(email: String) {
        _myAccount.value = email
    }

    private fun setTemperature(temp: Double) {
        _temperature.value = temp
    }

    private fun setCommentList(list: List<Comment>) {
        _commentList.value = list
    }

    private fun setLoginType(type: String) {
        _loginType.value = type
        Log.d(TAG, "setLoginType: ${loginType.value} ${loginType.value == Constant.KAKAO}")
    }


    fun setSelectedMonth(date: String) {
        _selectedMonth.value = date
    }

    fun setPasswordCorrect(isCorrect: Boolean) {
        _passwordCorrect.value = isCorrect
        _canChangePassword.value = passwordCorrect.value!! && passwordCheckCorrect.value!!
    }

    fun setPasswordCheckCorrect(isCorrect: Boolean) {
        _passwordCheckCorrect.value = isCorrect
        _canChangePassword.value = passwordCorrect.value!! && passwordCheckCorrect.value!!
    }

    fun setPushYN(yn: Char) {
        _pushYN.value = yn
    }

    fun signOut() = viewModelScope.launch {
        MyPageRepository.INSTANCE.signOut().apply{
            if (this.isSuccessful) {
                this.body()?.let { response ->
                    when (response.code) {
                        SUCCESS_CODE -> {
                            //todo toast vs snackbar
                            _snackMessage.value = "회원 탈퇴가 완료되었습니다."
                            CodaApplication.getInstance().signOut()
                        }
                        else -> onError(response.message)
                    }
                }
            } else {
                this.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        onError("회원 탈퇴에 실패하였습니다.")
                    }
                }
            }
        }
    }

    fun setResponseChangePassword(changePasswordRequest: ChangePasswordRequest) =
        viewModelScope.launch {
            _loading.postValue(true)
            var response: Response<IsSuccessResponse>? = null
            val job = launch(Dispatchers.Main + exceptionHandler) {
                response = MyPageRepository.INSTANCE.changePassword(changePasswordRequest)
            }
            job.join()
            _loading.postValue(false)
            response?.let {
                if (it.isSuccessful) {
                    it.body()?.let { response ->
                        when (it.code()) {
                            200 -> {
                                //todo toast vs snackbar 바로 화면 이동되는 경우
                                _snackMessage.value = "비밀번호를 변경하였습니다."
                            }
                            else -> onError(response.message)
                        }
                    }
                } else {
                    it.errorBody()?.let { errorBody ->
                        RetrofitClient.getErrorResponse(errorBody)?.let {
                            if (it.status == 401) {
                                onError("다시 로그인해 주세요.")
                                CodaApplication.getInstance().logOut()
                            } else {
                                onError("비밀번호 변경에 실패하였습니다.")
                            }
                        }
                    }
                }
            }
        }

    fun logout() = viewModelScope.launch {
        LogOutRepository.INSTANCE.logOut().apply {
            if (this.isSuccessful) {
                this.body()?.let { response ->
                    Log.d(TAG, "logout: $response")
                    when (response.code) {
                        SUCCESS_CODE -> {
                           
                        }
                    }
                }
            }
        }
        CodaApplication.getInstance().logOut()
    }

    fun setResponseGetMyPage() = viewModelScope.launch {

        _loading.postValue(true)
        var response: Response<MyPageResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = MyPageRepository.INSTANCE.getMyPage()
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    when (it.code()) {
                        200 -> {
                            Log.d(TAG, "setResponseGetMyPage: ${response.result}")
                            setMyAccount(response.result.email)
                            setTemperature(response.result.temperature)
                            setPushYN(response.result.pushYN)
                            setLoginType(response.result.loginType)
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
                            _snackMessage.postValue("내 정보를 불러오는 데 실패하였습니다.")
                        }
                    }
                }
            }
        }
    }

    fun getResponseCommentList(date: String) = viewModelScope.launch {

        _loading.postValue(true)
        var response: Response<CommentListResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = if (date == "all")
                MyPageRepository.INSTANCE.getAllComment()
            else
                MyPageRepository.INSTANCE.getMonthComment(date)
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    when (it.code()) {
                        200 -> {
                            setCommentList(response.result)
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
//                            CodaSnackBar.make(binding.root, "코멘트를 받아오는 데 실패했습니다.")
                            onError(it.message)
                        }
                    }
                }
            }
        }
    }

    fun setResponsePatchCommentPushState() = viewModelScope.launch {
        _loading.postValue(true)
        var response: Response<CommentPushStateResponse>? = null
        val job = launch(Dispatchers.Main + exceptionHandler) {
            response = MyPageRepository.INSTANCE.patchCommentPushState()
        }
        job.join()
        _loading.postValue(false)
        response?.let {
            if (it.isSuccessful) {
                it.body()?.let { response ->
                    when (it.code()) {
                        200 -> {
                            response.result["pushYn"]?.let { yn -> setPushYN(yn) }
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
        _loading.value = false

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}