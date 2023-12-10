package com.movingmaker.presentation.viewmodel.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.movingmaker.presentation.base.BaseViewModel
import com.movingmaker.presentation.util.FRAGMENT_NAME
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class OnboardingViewModel : BaseViewModel() {

    private var _currentFragment = MutableLiveData<FRAGMENT_NAME>().apply {
        FRAGMENT_NAME.SIGNUP_TERMS
    }

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

    private suspend fun clearCachedDiaries(userId: Long) {}
}