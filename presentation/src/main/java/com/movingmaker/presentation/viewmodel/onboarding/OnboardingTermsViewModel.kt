package com.movingmaker.presentation.viewmodel.onboarding

import androidx.lifecycle.viewModelScope
import com.movingmaker.domain.repository.PreferencesRepository
import com.movingmaker.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TermsScreenUiEvent {
    data object GoPolicy : TermsScreenUiEvent
    data object GoTerms : TermsScreenUiEvent
    data class Submit(
        val isPushAccept: Boolean
    ) : TermsScreenUiEvent
}

@HiltViewModel
class OnboardingTermsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : BaseViewModel() {

    private var _uiEvent = MutableSharedFlow<TermsScreenUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var _isTermsAccept = MutableStateFlow<Boolean>(false)
    val isTermsAccept = _isTermsAccept.asStateFlow()

    private var _isPolicyAccept = MutableStateFlow<Boolean>(false)
    val isPolicyAccept = _isPolicyAccept.asStateFlow()

    private var _isPushAccept = MutableStateFlow<Boolean>(false)
    val isPushAccept = _isPushAccept.asStateFlow()

    private var _isAllAccept = MutableStateFlow<Boolean>(false)
    val isAllAccept = _isAllAccept.asStateFlow()

    fun onClickTermsAccept() {
        _isTermsAccept.value = _isTermsAccept.value xor true
        checkAllAccepted()
    }

    fun onClickPolicyAccept() {
        _isPolicyAccept.value = _isPolicyAccept.value xor true
        checkAllAccepted()
    }

    fun onClickPushAccept() {
        _isPushAccept.value = _isPushAccept.value xor true
        checkAllAccepted()
    }

    fun onClickAcceptAll() {
        if (isAllAccept.value) {
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

    fun onClickGoPolicy() {
        viewModelScope.launch {
            _uiEvent.emit(TermsScreenUiEvent.GoPolicy)
        }
    }

    fun onClickGoTerms() {
        viewModelScope.launch {
            _uiEvent.emit(TermsScreenUiEvent.GoTerms)
        }
    }

    fun onClickSubmit() {
        viewModelScope.launch {
            setPreferences()
            _uiEvent.emit(TermsScreenUiEvent.Submit(isPushAccept.value))
        }
    }

    private suspend fun setPreferences() {
        preferencesRepository.setIsTermsAccept(isTermsAccept.value)
        preferencesRepository.setIsPolicyAccept(isPolicyAccept.value)
        preferencesRepository.setIsPushAccept(isPushAccept.value)
    }

    private fun checkAllAccepted() {
        _isAllAccept.value = isPolicyAccept.value && isTermsAccept.value && isPushAccept.value
    }

    private suspend fun clearCachedDiaries(userId: Long) {}
}