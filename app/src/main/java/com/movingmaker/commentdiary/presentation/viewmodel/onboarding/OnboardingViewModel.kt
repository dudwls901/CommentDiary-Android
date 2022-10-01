package com.movingmaker.commentdiary.presentation.viewmodel.onboarding

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.*
import com.movingmaker.commentdiary.common.CodaApplication
import com.movingmaker.commentdiary.data.repository.ForSignUpRespository
import com.movingmaker.commentdiary.data.repository.MyPageRepository
import com.movingmaker.commentdiary.common.util.Constant.EMAIL
import com.movingmaker.commentdiary.common.util.Constant.KAKAO
import com.movingmaker.commentdiary.common.util.Constant.SUCCESS_CODE
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import kotlinx.coroutines.*

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}", "log")
    }

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _emailCorrect = MutableLiveData<Boolean>()
    val emailCorrect: LiveData<Boolean>
        get() = _emailCorrect

    private var _passwordCorrect = MutableLiveData<Boolean>().apply { value = true }
    val passwordCorrect: LiveData<Boolean>
        get() = _passwordCorrect

    private var _passwordCheckCorrect = MutableLiveData<Boolean>().apply { value = true }
    val passwordCheckCorrect: LiveData<Boolean>
        get() = _passwordCheckCorrect

    private var _canMakeAccount = MutableLiveData<Boolean>()
    val canMakeAccount: LiveData<Boolean>
        get() = _canMakeAccount

    private var _currentFragment = MutableLiveData<FRAGMENT_NAME>().apply {
        FRAGMENT_NAME.LOGIN_BEFORE
    }
    val currentFragment: LiveData<FRAGMENT_NAME>
        get() = _currentFragment

    private var _loginCorrect = MutableLiveData<Boolean>()
    val loginCorrect: LiveData<Boolean>
        get() = _loginCorrect

    private var _loginNotice = MutableLiveData<String>()
    val loginNotice: LiveData<String>
        get() = _loginNotice

    //for signUp, logIn data share
    private var _isTermsAccept = MutableLiveData<Boolean>().apply { value = false }
    val isTermsAccept: LiveData<Boolean>
        get() = _isTermsAccept

    private var _isPolicyAccept = MutableLiveData<Boolean>().apply { value = false }
    val isPolicyAccept: LiveData<Boolean>
        get() = _isPolicyAccept

    private var _isPushAccept = MutableLiveData<Boolean>().apply { value = false }
    val isPushAccept: LiveData<Boolean>
        get() = _isPushAccept

    private var _isAllAccept = MutableLiveData<Boolean>().apply { value = false }
    val isAllAccept: LiveData<Boolean>
        get() = _isAllAccept

    private var _email = MutableLiveData<String>().apply { value = "" }
    val email: LiveData<String>
        get() = _email

    private var _emailNotice = MutableLiveData<String>()
    val emailNotice: LiveData<String>
        get() = _emailNotice

    private var _code = MutableLiveData<Int>()
    val code: LiveData<Int>
        get() = _code

    private var _codeCorrect = MutableLiveData<Boolean>().apply { value = true }
    val codeCorrect: LiveData<Boolean>
        get() = _codeCorrect

    private var _password = MutableLiveData<String>().apply { value = "" }
    val password: LiveData<String>
        get() = _password

    private var _checkPassword = MutableLiveData<String>().apply { value = "" }
    val checkPassword: LiveData<String>
        get() = _checkPassword

    //for findPassword
    private var _findPasswordEmail = MutableLiveData<String>().apply { value = "" }
    private val findPasswordEmail: LiveData<String>
        get() = _findPasswordEmail

    private var _findPasswordEmailCorrect = MutableLiveData<Boolean>()
    val findPasswordEmailCorrect: LiveData<Boolean>
        get() = _findPasswordEmailCorrect

    private var _successFindPassword = MutableLiveData<Boolean>()
    val successFindPassword: LiveData<Boolean>
        get() = _successFindPassword

    private var _findPasswordEmailNotice = MutableLiveData<String>()
    val findPasswordEmailNotice: LiveData<String>
        get() = _findPasswordEmailNotice

    private var _shakeView = MutableLiveData<Boolean>()
    val shakeView: LiveData<Boolean>
        get() = _shakeView

    init {
        _canMakeAccount.value = false
        _loginCorrect.value = true
    }

    private fun setValuesIsCorrect(isCorrect: Boolean, type: String) {
        when (type) {
            "email" -> {
                _emailCorrect.value = isCorrect
            }
            "password" -> {
                _passwordCorrect.value = isCorrect
            }
            "passwordCheck" -> {
                _passwordCheckCorrect.value = isCorrect
            }
            "findPasswordEmail" -> {
                _findPasswordEmailCorrect.value = isCorrect
            }
        }
    }

    private fun setCanMakeAccount(isCanMakeAccount: Boolean) {
        _canMakeAccount.value =
            emailCorrect.value!! && passwordCorrect.value!! && passwordCheckCorrect.value!! && isCanMakeAccount
    }

    fun setCurrentFragment(fragment: FRAGMENT_NAME) {
        _currentFragment.value = fragment
    }

    fun setTermsAccept() {
        _isTermsAccept.value = _isTermsAccept.value!! xor true
        checkAllAccepted()
    }

    fun setPolicyAccept() {
        _isPolicyAccept.value = _isPolicyAccept.value!! xor true
        checkAllAccepted()
    }

    fun setPushAccept() {
        _isPushAccept.value = _isPushAccept.value!! xor true
        checkAllAccepted()
    }

    private fun checkAllAccepted() {
        _isAllAccept.value =
            isPolicyAccept.value == true && isTermsAccept.value == true && isPushAccept.value == true
    }

    fun setAllAccept() {
        if (isAllAccept.value == true) {
            _isTermsAccept.value = false
            _isPolicyAccept.value = false
            _isPushAccept.value = false
            _isAllAccept.value = false
        } else {
            _isTermsAccept.value = true
            _isPolicyAccept.value = true
            _isPushAccept.value = true
            _isAllAccept.value = true
        }
    }

    fun setEmail(text: String) {
        _email.value = text
    }

    fun setPassword(text: String) {
        _password.value = text
    }

    fun setCheckPassword(text: String) {
        _checkPassword.value = text
    }

    fun setFindPasswordEmail(text: String) {
        _findPasswordEmail.value = text
    }

    fun setFindPasswordEmailCorrect(isCorrect: Boolean) {
        _findPasswordEmailCorrect.value = isCorrect
    }

    fun setFindPasswordEmailNotice(text: String) {
        _findPasswordEmailNotice.value = text
    }

    fun setEmailNotice(text: String) {
        _emailNotice.value = text
    }

    fun setLoginNotice(text: String) {
        _loginNotice.value = text
    }

    private fun setSuccessFindPassword(isSuccess: Boolean) {
        _successFindPassword.value = isSuccess
    }

    fun setEmailCode(text: String) {
        if (text.isNotBlank())
            _code.value = text.toInt()
    }

    fun setCodeCorrect(isCorrect: Boolean) {
        _codeCorrect.value = isCorrect
    }

    fun setShakeView(isShake: Boolean) {
        _shakeView.value = isShake
    }

    fun setEmailCorrect(isCorrect: Boolean) {
        _emailCorrect.value = isCorrect
    }

    fun validateEmail(type: String) {
        val tempEmail = if (type == "email") email.value!! else findPasswordEmail.value!!
        val isEmailCorrect = !(tempEmail.isNotEmpty() &&
                (tempEmail.indexOf('@') == -1 || tempEmail.indexOf('.') == -1) ||
                (tempEmail.indexOf('@') != -1 &&
                        tempEmail.substring(tempEmail.indexOf('@'))
                            .indexOf('.') == -1))
        if (type == "email") {
            setValuesIsCorrect(isEmailCorrect, "email")
        } else {
            setValuesIsCorrect(isEmailCorrect, "findPasswordEmail")
        }
    }

    fun validatePassword() {
        var hasLetter = false
        var hasNum = false
        var hasSign = false
        val tempPassword = password.value!!
        val tempCheckPassword = checkPassword.value!!
        for (ch in tempPassword) {
            when {
                ch.isLetter() -> hasLetter = true
                ch.isDigit() -> hasNum = true
                else -> hasSign = true
            }
        }
        val isPasswordCorrect =
            !(tempPassword.isNotEmpty() && tempPassword != "" && (tempPassword.length < 8 || !hasLetter || !hasNum || !hasSign || tempPassword.length > 16))
        val isPasswordCheckCorrect =
            !(tempCheckPassword.isNotEmpty() && (tempPassword != tempCheckPassword))
        setValuesIsCorrect(isPasswordCorrect, "password")
        setValuesIsCorrect(isPasswordCheckCorrect, "passwordCheck")
        setCanMakeAccount(
            tempPassword.isNotEmpty() && tempCheckPassword.isNotEmpty()
        )
    }

    fun validateCheckPassword() {
        val tempPassword = password.value!!
        val tempCheckPassword = checkPassword.value!!

        val isPasswordCheckCorrect =
            !(tempPassword.isNotEmpty() && (tempPassword != tempCheckPassword))

        setValuesIsCorrect(isPasswordCheckCorrect, "passwordCheck")
        setCanMakeAccount(
            tempPassword.isNotEmpty() && tempCheckPassword.isNotEmpty()
        )
    }

    suspend fun emailCodeSend() = viewModelScope.async {
        if (email.value == null || email.value == "" || emailCorrect.value != true) {
            offLoading()
            setShakeView(true)
            return@async false
        }
        var successCodeSend = false
        ForSignUpRespository.INSTANCE.sendEmailCode(email = email.value!!).apply {
            offLoading()
            if (this.isSuccessful) {
                this.body()?.let { response ->
                    when (response.code) {
                        SUCCESS_CODE -> {
                            successCodeSend = true
                        }
                    }
                }
            } else {
                this.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        setEmailNotice(it.message)
                        setShakeView(true)
                    }
                }
            }
        }
        successCodeSend
    }.await()

    suspend fun checkCode() = viewModelScope.async {
        if (email.value == null || code.value == null) {
            offLoading()
            setCodeCorrect(false)
            setShakeView(true)
            return@async false
        }
        ForSignUpRespository.INSTANCE.emailCodeCheck(
            EmailCodeCheckRequest(
                email.value!!,
                code.value!!
            )
        ).apply {
            offLoading()
            if (this.isSuccessful) {
                this.body()?.let { response ->
                    when (response.code) {
                        SUCCESS_CODE -> {
                            setCodeCorrect(true)
                        }
                    }
                }
            } else {
                this.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        onError(it.message, "text")
                        setCodeCorrect(false)
                        setShakeView(true)
                    }
                }
            }
        }
        codeCorrect.value!!
    }.await()

    suspend fun signUp() = viewModelScope.async {
        if (canMakeAccount.value != true) {
            offLoading()
            setShakeView(true)
            return@async false
        }
        var successSignUp = false
        ForSignUpRespository.INSTANCE.signUp(
            SignUpRequest(
                email = email.value!!,
                password = password.value!!,
                checkPassword = checkPassword.value!!,
                loginType = EMAIL
            )
        ).apply {
            offLoading()
            if (this.isSuccessful) {
                this.body()?.let { response ->
                    when (response.code) {
                        SUCCESS_CODE -> {
                            successSignUp = true
                        }
                    }
                }
            } else {
                this.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        onError(it.message, "text")
                        setShakeView(true)
                    }
                }
            }
        }
        successSignUp
    }.await()


    suspend fun findPassword() = viewModelScope.async {
        if (findPasswordEmail.value == null || findPasswordEmailCorrect.value != true) {
            offLoading()
            setShakeView(true)
            setSuccessFindPassword(false)
            return@async false
        }
        ForSignUpRespository.INSTANCE.findPassword(findPasswordEmail.value!!).apply {
            offLoading()
            if (this.isSuccessful) {
                this.body()?.let { response ->
                    when (response.code) {
                        SUCCESS_CODE -> {
                            setSuccessFindPassword(true)
                        }
                        else -> {
                        }
                    }
                }
            } else {
                this.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        setSuccessFindPassword(false)
                        setShakeView(true)
                        _findPasswordEmailNotice.value = it.message
                    }
                }
            }
        }
        _successFindPassword.value!!
    }.await()

    suspend fun login() = viewModelScope.async {
        if (emailCorrect.value == false) {
            offLoading()
            setLoginNotice("이메일을 확인해 주세요.")
            setShakeView(true)
            return@async false
        }
        if (email.value!!.isEmpty() || password.value!!.isEmpty()) {
            offLoading()
            setShakeView(true)
            setLoginNotice("이메일 혹은 비밀번호가 올바르지 않습니다.")
            return@async false
        }
        var successLogin = false
        ForSignUpRespository.INSTANCE.logIn(
            LogInRequest(
                email = email.value!!,
                password = password.value!!,
                CodaApplication.deviceToken
            )
        ).apply {
            offLoading()
            if (this.isSuccessful) {
                this.body()?.let { response ->
                    when (response.code) {
                        SUCCESS_CODE -> {
                            Log.d("login", "login: $response")
                            val accessToken = response.result.accessToken
                            val refreshToken = response.result.refreshToken
                            val accessTokenExpiresIn = response.result.accessTokenExpiresIn
                            Log.d(
                                "login",
                                "login: ${accessToken} ${refreshToken} ${accessTokenExpiresIn}"
                            )
                            CodaApplication.getInstance()
                                .insertAuth(EMAIL, accessToken, refreshToken, accessTokenExpiresIn)
                            successLogin = true
                        }
                    }
                }
            } else {
                this.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        setLoginNotice(it.message)
                        setShakeView(true)
                    }
                }
            }
        }
        successLogin
    }.await()

    suspend fun kakaoLogin(kakaoAccessToken: String) = viewModelScope.async {
        var successLogin = false
        var isNewMember = false
        ForSignUpRespository.INSTANCE.kakaoLogin(
            KakaoLoginRequest(
                KAKAO,
                kakaoAccessToken,
                CodaApplication.deviceToken
            )
        ).apply {
            offLoading()
            if (this.isSuccessful) {
                this.body()?.let { loginResponse ->
                    when (loginResponse.code) {
                        SUCCESS_CODE -> {
                            successLogin = true
                            Log.d("kakaoLogin", "kakaoLogin: $loginResponse")
                            val accessToken = loginResponse.result.accessToken
                            val refreshToken = loginResponse.result.refreshToken
                            val accessTokenExpiresIn = loginResponse.result.accessTokenExpiresIn
                            isNewMember = loginResponse.result.isNewMember == true
                            Log.d(
                                "kakaoLogin",
                                "kakaoLogin: ${accessToken} ${refreshToken} ${accessTokenExpiresIn}"
                            )
                            CodaApplication.getInstance()
                                .insertAuth(KAKAO, accessToken, refreshToken, accessTokenExpiresIn)
                        }
                    }

                }
            } else {
                this.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        //토스트든 스낵바든 띄우기
                        onError(it.message, "toast")
                    }
                }
            }
        }
        Pair(successLogin,isNewMember)
    }.await()

    suspend fun kakaoSignUpSetAccepts() = viewModelScope.async {
        var successSignUp = false
        val kakaoSignUpRequest =
            if (isPushAccept.value == true) KakaoSignUpRequest('Y') else KakaoSignUpRequest('N')
        MyPageRepository.INSTANCE.kakaoSignUpSetAccepts(kakaoSignUpRequest).apply {
            offLoading()
            if (this.isSuccessful) {
                this.body()?.let { loginResponse ->
                    when (loginResponse.code) {
                        SUCCESS_CODE -> {
                            successSignUp = true
                        }
                    }

                }
            } else {
                successSignUp = false
                this.errorBody()?.let { errorBody ->
                    RetrofitClient.getErrorResponse(errorBody)?.let {
                        //토스트든 스낵바든 띄우기
                        onError(it.message, "toast")
                    }
                }
            }
        }
        successSignUp
    }.await()

    suspend fun signOut() = viewModelScope.async {
        var successSignOut = false
        MyPageRepository.INSTANCE.signOut().apply{
            if (this.isSuccessful) {
                this.body()?.let { response ->
                    when (response.code) {
                        SUCCESS_CODE -> {
                            successSignOut = true
                            CodaApplication.getInstance().signOut()
                        }
                    }
                }
            } else {
                this.errorBody()?.let { errorBody ->
                }
            }
        }
        successSignOut
    }.await()



    fun onLoading() {
        _loading.postValue(true)
    }

    fun offLoading() {
        _loading.postValue(false)
    }

    private fun onError(message: String, type: String) {
//        _errorMessage.value = message
        Log.d("onboardingViewModelError", "onError: $message")
        when (type) {
            "text" -> {

            }
            "toast" -> {

            }
        }
        offLoading()
    }
}