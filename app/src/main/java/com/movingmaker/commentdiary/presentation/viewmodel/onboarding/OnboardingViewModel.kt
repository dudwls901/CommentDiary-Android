package com.movingmaker.commentdiary.presentation.viewmodel.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoSignUpRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.usecase.EmailCodeCheckUseCase
import com.movingmaker.commentdiary.domain.usecase.FindPasswordUseCase
import com.movingmaker.commentdiary.domain.usecase.KakaoLogInUseCase
import com.movingmaker.commentdiary.domain.usecase.KakaoSignUpSetAcceptsUseCase
import com.movingmaker.commentdiary.domain.usecase.LogInUseCase
import com.movingmaker.commentdiary.domain.usecase.SendEmailCodeUseCase
import com.movingmaker.commentdiary.domain.usecase.SignOutUseCase
import com.movingmaker.commentdiary.domain.usecase.SignUpUseCase
import com.movingmaker.commentdiary.presentation.CodaApplication
import com.movingmaker.commentdiary.presentation.base.BaseViewModel
import com.movingmaker.commentdiary.presentation.util.EMAIL
import com.movingmaker.commentdiary.presentation.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.presentation.util.KAKAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sendEmailCodeUseCase: SendEmailCodeUseCase,
    private val sendEmailCodeCheckUseCase: EmailCodeCheckUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val findPasswordUseCase: FindPasswordUseCase,
    private val logInUseCase: LogInUseCase,
    private val kakaoLogInUseCase: KakaoLogInUseCase,
    private val kakaoSignUpSetAcceptsUseCase: KakaoSignUpSetAcceptsUseCase,
    private val signOutUseCase: SignOutUseCase
) : BaseViewModel() {

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
    private val canMakeAccount: LiveData<Boolean>
        get() = _canMakeAccount

    private var _currentFragment = MutableLiveData<FRAGMENT_NAME>().apply {
        FRAGMENT_NAME.LOGIN_BEFORE
    }
    val currentFragment: LiveData<FRAGMENT_NAME>
        get() = _currentFragment

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

    var email = MutableLiveData<String>().apply { value = "" }
    var password = MutableLiveData<String>().apply { value = "" }

    private var _emailNotice = MutableLiveData<String>()
    val emailNotice: LiveData<String>
        get() = _emailNotice

    var code = MutableLiveData<String>()

    private var _codeCorrect = MutableLiveData<Boolean>().apply { value = true }
    val codeCorrect: LiveData<Boolean>
        get() = _codeCorrect

    var checkPassword = MutableLiveData<String>().apply { value = "" }

    //for findPassword
    var findPasswordEmail = MutableLiveData<String>().apply { value = "" }

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

    fun clearEmail() {
        email.value = ""
    }

    fun clearPassword() {
        password.value = ""
    }

    fun clearCheckPassword() {
        checkPassword.value = ""
    }

    fun clearFindPasswordEmail() {
        findPasswordEmail.value = ""
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

    fun clearCode() {
        code.value = ""
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
        val isEmailCorrect =
            !(tempEmail.isNotEmpty() && (tempEmail.indexOf('@') == -1 || tempEmail.indexOf('.') == -1) || (tempEmail.indexOf(
                '@'
            ) != -1 && tempEmail.substring(tempEmail.indexOf('@')).indexOf('.') == -1))
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
        onLoading()
        if (email.value == null || email.value == "" || emailCorrect.value != true) {
            offLoading()
            setShakeView(true)
            return@async false
        }
        var successCodeSend = false
        with(sendEmailCodeUseCase(email = email.value!!)) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    successCodeSend = true
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setEmailNotice(this.message)
                    setShakeView(true)
                }
            }
        }
        successCodeSend
    }.await()

    suspend fun checkCode() = viewModelScope.async {
        onLoading()
        if (email.value == null || code.value == null) {
            offLoading()
            setCodeCorrect(false)
            setShakeView(true)
            return@async false
        }
        with(
            sendEmailCodeCheckUseCase(
                EmailCodeCheckRequest(
                    email.value!!, code.value!!.toInt()
                )
            )
        ) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    setCodeCorrect(true)
                }
                is UiState.Error -> {
                    setMessage(message)
                    setCodeCorrect(false)
                }
                is UiState.Fail -> {
                    setCodeCorrect(false)
                    setShakeView(true)
                }
            }
        }
        codeCorrect.value!!
    }.await()

    suspend fun signUp() = viewModelScope.async {
        onLoading()
        if (canMakeAccount.value != true) {
            offLoading()
            setShakeView(true)
            return@async false
        }
        var successSignUp = false
        with(
            signUpUseCase(
                SignUpRequest(
                    email = email.value!!,
                    password = password.value!!,
                    checkPassword = checkPassword.value!!,
                    loginType = EMAIL
                )
            )
        ) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    successSignUp = true
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setShakeView(true)
                }
            }
        }
        successSignUp
    }.await()


    suspend fun findPassword() = viewModelScope.async {
        if (findPasswordEmail.value == null || findPasswordEmailCorrect.value != true) {
            setShakeView(true)
            _successFindPassword.value = false
            return@async false
        }
        onLoading()
        with(findPasswordUseCase(findPasswordEmail.value!!)) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    _successFindPassword.value = true
                }
                is UiState.Error -> {
                    setMessage(message)
                    _successFindPassword.value = false
                }
                is UiState.Fail -> {
                    setShakeView(true)
                    _findPasswordEmailNotice.value = this.message
                    _successFindPassword.value = false

                }
            }
        }
        _successFindPassword.value!!
    }.await()

    suspend fun login() = viewModelScope.async {
        onLoading()
        if (emailCorrect.value == false) {
            offLoading()
            setLoginNotice("이메일을 확인해 주세요.")
            setShakeView(true)
            return@async false
        }
        if (email.value!!.isEmpty() || password.value!!.isEmpty()) {
            offLoading()
            setShakeView(true)
            Timber.d("${email.value} ${password.value}")
            setLoginNotice("이메일 혹은 비밀번호가 올바르지 않습니다.")
            return@async false
        }
        var isSuccessLogin = false
        logInUseCase(
            LogInRequest(
                email = email.value!!, password = password.value!!, CodaApplication.deviceToken
            )
        ).apply {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    val accessToken = this.data.accessToken
                    val refreshToken = this.data.refreshToken
                    val accessTokenExpiresIn = this.data.accessTokenExpiresIn
                    Timber.d("login: ${accessToken} ${refreshToken} ${accessTokenExpiresIn}")
                    CodaApplication.getInstance()
                        .insertAuth(EMAIL, accessToken, refreshToken, accessTokenExpiresIn)
                    isSuccessLogin = true
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setShakeView(true)
                    _loginNotice.value = this.message
                }
            }
        }
        isSuccessLogin
    }.await()

    suspend fun kakaoLogin(kakaoAccessToken: String) = viewModelScope.async {
        var successLogin = false
        var isNewMember = false
        with(
            kakaoLogInUseCase(
                KakaoLoginRequest(
                    KAKAO, kakaoAccessToken, CodaApplication.deviceToken
                )
            )
        ) {
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    successLogin = true
                    val accessToken = this.data.accessToken
                    val refreshToken = this.data.refreshToken
                    val accessTokenExpiresIn = this.data.accessTokenExpiresIn
                    isNewMember = this.data.isNewMember == true
                    Timber.d(
                        "kakaoLogin: ${accessToken} ${refreshToken} ${accessTokenExpiresIn}"
                    )
                    CodaApplication.getInstance()
                        .insertAuth(KAKAO, accessToken, refreshToken, accessTokenExpiresIn)
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
        Pair(successLogin, isNewMember)
    }.await()

    suspend fun kakaoSignUpSetAccepts() = viewModelScope.async {
        onLoading()
        var successSignUp = false
        val kakaoSignUpRequest =
            if (isPushAccept.value == true) KakaoSignUpRequest('Y') else KakaoSignUpRequest('N')
        with(kakaoSignUpSetAcceptsUseCase(kakaoSignUpRequest)) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    successSignUp = true
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
        successSignUp
    }.await()

    suspend fun signOut() = viewModelScope.async {
        onLoading()
        var successSignOut = false
        with(signOutUseCase()) {
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    successSignOut = true
                    CodaApplication.getInstance().signOut()
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
        successSignOut
    }.await()

}