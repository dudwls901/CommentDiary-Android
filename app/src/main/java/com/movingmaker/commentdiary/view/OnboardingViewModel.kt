package com.movingmaker.commentdiary.view

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.movingmaker.commentdiary.model.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.model.remote.request.SignUpRequest
import com.movingmaker.commentdiary.model.remote.response.EmailCodeResponse
import com.movingmaker.commentdiary.model.repository.ForSignUpRespository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private var _currentNum = MutableLiveData<Int>()
    private var _emailCorrect = MutableLiveData<Boolean>()
    private var _passwordCorrect = MutableLiveData<Boolean>()
    private var _passwordCheckCorrect = MutableLiveData<Boolean>()
    private var _canMakeAccount = MutableLiveData<Boolean>()
    private var _currentFragment = MutableLiveData<String>()
    private var _responseEmailSend = MutableLiveData<Response<EmailCodeResponse>>()
    private var _responseEmailCodeCheck = MutableLiveData<Response<EmailCodeResponse>>()
    private var _emailCodeCheckComplete = MutableLiveData<Boolean>()
    private var _responseSignUpComplete = MutableLiveData<Response<EmailCodeResponse>>()
    private var _email = MutableLiveData<String>()
    private var _password = MutableLiveData<String>()
    private var _checkPassword = MutableLiveData<String>()

    val emailCorrect: LiveData<Boolean>
        get() = _emailCorrect

    val passwordCorrect: LiveData<Boolean>
        get() = _passwordCorrect

    val passwordCheckCorrect: LiveData<Boolean>
        get() = _passwordCheckCorrect

    val canMakeAccount: LiveData<Boolean>
        get() = _canMakeAccount

    val currentFragment: LiveData<String>
        get() = _currentFragment

    val responseEmailSend: LiveData<Response<EmailCodeResponse>>
        get() = _responseEmailSend

    val responseEmailCodeCheck: LiveData<Response<EmailCodeResponse>>
        get() = _responseEmailCodeCheck

    val emailCodeCheckComplete: LiveData<Boolean>
        get() = _emailCodeCheckComplete

    val responseSignUpComplete: LiveData<Response<EmailCodeResponse>>
        get() = _responseSignUpComplete

    val email: LiveData<String>
        get() = _email

    val passwrod: LiveData<String>
        get() = _password

    val checkPassword: LiveData<String>
        get() = _checkPassword

    init {
        _currentNum.value = 0
        _emailCorrect.value = true
        _passwordCorrect.value = true
        _passwordCheckCorrect.value = true
        _canMakeAccount.value = false
        _currentFragment.value = ""
        _emailCodeCheckComplete.value = false
        _email.value = ""
        _password.value = ""
        _checkPassword.value = ""
    }

    fun setIsCorrect(isCorrect: Boolean, type: String) {
        when (type) {
            "email" -> {
                _emailCorrect.value = isCorrect
            }
            "password" -> {
                _passwordCorrect.value = isCorrect
            }
            "passwordCheck" -> {
                _passwordCheckCorrect.value = isCorrect
                Log.d(TAG, "setIsCorrect: ${passwordCheckCorrect.value}")
            }
        }
    }

    fun setCanMakeAccount(isCanMakeAccount: Boolean) {
        _canMakeAccount.value =
            emailCorrect.value!! && passwordCorrect.value!! && passwordCheckCorrect.value!! && isCanMakeAccount
    }

    fun setCurrentFragment(fragment: String) {
        _currentFragment.value = fragment
    }

    fun setEmailCodeCheckComplete(isComplete: Boolean) {
        _emailCodeCheckComplete.value = isComplete
    }

    fun setEmail(text: String){
        _email.value = text
    }

    fun setPassword(text: String){
        _password.value = text
    }

    fun setCheckPassword(text: String){
        _checkPassword.value = text
    }

    suspend fun setResponseEmailSend(email: String) {
            withContext(viewModelScope.coroutineContext) {
                _responseEmailSend.value =
                    ForSignUpRespository.INSTANCE.sendEmailCode(email = email)
            }
    }

    suspend fun setResponseEmailCodeCheck(email: String, code: Int) {
        withContext(viewModelScope.coroutineContext) {
            _responseEmailCodeCheck.value = ForSignUpRespository.INSTANCE.emailCodeCheck(
                EmailCodeCheckRequest(email, code)
            )
        }
    }

    suspend fun setResponseSignUp() {
        withContext(viewModelScope.coroutineContext) {
            _responseSignUpComplete.value = ForSignUpRespository.INSTANCE.signUp(
                SignUpRequest(
                    email = email.value!!,
                    password = passwrod.value!!,
                    checkPassword = checkPassword.value!!
                )
            )
        }
    }



//    fun setResponseEmailSend(email: String){
//        val call = RetrofitClient.onboardingApiService.sendEmail(email = email)
//        call.enqueue(object : retrofit2.Callback<SendEmailCodeResponse>{
//            //응답 성공시
//            override fun onResponse(call: Call<SendEmailCodeResponse>, response: Response<SendEmailCodeResponse>) {
//                Log.d(TAG, "onResponse(): called / response : ${response.raw()}")
////                completion(RESPONSE_STATE.OKAY,response.body().toString())
//            }
//
//            //응답 실패시
//            override fun onFailure(call: Call<SendEmailCodeResponse>, t: Throwable) {
//                Log.d(TAG, "RetrofitManager - onFailure(): called /t : $t")
////                completion(RESPONSE_STATE.FAIL,t.toString())
//            }
//
//        })
//    }
}