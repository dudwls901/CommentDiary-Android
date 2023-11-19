package com.movingmaker.presentation.viewmodel.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.usecase.ClearCachedDiariesUseCase
import com.movingmaker.domain.usecase.SignOutUseCase
import com.movingmaker.presentation.base.BaseViewModel
import com.movingmaker.presentation.util.EMPTY_USER
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.PreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesUtil: PreferencesUtil,
    private val signOutUseCase: SignOutUseCase,
    private val clearCachedDiariesUseCase: ClearCachedDiariesUseCase
) : BaseViewModel() {

    private var _currentFragment = MutableLiveData<FRAGMENT_NAME>().apply {
        FRAGMENT_NAME.SIGNUP_TERMS
    }
    val currentFragment: LiveData<FRAGMENT_NAME>
        get() = _currentFragment

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

    var code = MutableLiveData<String>()

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

//    suspend fun kakaoLogin(kakaoAccessToken: String) = viewModelScope.async {
//        var successLogin = false
//        var isNewMember = false
//        with(
//            kakaoLogInUseCase(
//                KakaoLoginModel(
//                    KAKAO, kakaoAccessToken, preferencesUtil.getDeviceToken()
//                )
//            )
//        ) {
//            when (this) {
//                is UiState.Success -> {
//                    successLogin = true
//                    val accessToken = this.data.authTokens.accessToken
//                    val refreshToken = this.data.authTokens.refreshToken
//                    val accessTokenExpiresIn = this.data.authTokens.accessTokenExpiresIn
//                    val userId = this.data.userId
//                    isNewMember = this.data.authTokens.isNewMember == true
//                    Timber.d(
//                        "kakaoLogin: ${accessToken} ${refreshToken} ${accessTokenExpiresIn}"
//                    )
//                    clearCachedDiaries(userId)
//                    preferencesUtil
//                        .insertAuth(KAKAO, accessToken, refreshToken, accessTokenExpiresIn, userId)
//                }
//                is UiState.Error -> {
//                    setMessage(message)
//                }
//                is UiState.Fail -> {
//                    setMessage(message)
//                }
//            }
//        }
//        Pair(successLogin, isNewMember)
//    }.await()

    private suspend fun clearCachedDiaries(userId: Long) {
        val beforeUserId = preferencesUtil.getUserId()
        if (beforeUserId != EMPTY_USER && beforeUserId != userId) {
            clearCachedDiariesUseCase()
        }
    }

//    suspend fun kakaoSignUpSetAccepts() = viewModelScope.async {
//        onLoading()
//        var successSignUp = false
//        val kakaoSignUpRequest =
//            KakaoSignUpModel(if (isPushAccept.value == true) 'Y' else 'N')
//        with(kakaoSignUpSetAcceptsUseCase(kakaoSignUpRequest)) {
//            offLoading()
//            Timber.d("result $this")
//            when (this) {
//                is UiState.Success -> {
//                    successSignUp = true
//                }
//                is UiState.Error -> {
//                    setMessage(message)
//                }
//                is UiState.Fail -> {
//                    setMessage(message)
//                }
//            }
//        }
//        successSignUp
//    }.await()

    suspend fun signOut() = viewModelScope.async {
        onLoading()
        var successSignOut = false
        with(signOutUseCase()) {
            when (this) {
                is UiState.Success -> {
                    successSignOut = true
                    preferencesUtil.signOut()
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