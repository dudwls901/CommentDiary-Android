package com.movingmaker.presentation.viewmodel.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.movingmaker.domain.model.UiState
import com.movingmaker.domain.model.User
import com.movingmaker.domain.usecase.RegisterUserUseCase
import com.movingmaker.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginScreenUiEvent {
    sealed class Login : LoginScreenUiEvent {
        data object Start : Login()
        data object Success : Login()
        data object Fail : Login()
    }
}

@HiltViewModel
class OnboardingLoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val registerUserUseCase: RegisterUserUseCase,
) : BaseViewModel() {

    private var _uiEvent = MutableSharedFlow<LoginScreenUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun registerUser(user: FirebaseUser?) {
        viewModelScope.launch {
            val uid = user?.uid ?: return@launch
            val email = user.email ?: return@launch
            when (
                registerUserUseCase(
                    User(
                        uid,
                        email,
                        permissions = mapOf(
                            "push" to (savedStateHandle["pushAccept"] ?: false)
                        )
                    )
                )
            ) {
                is UiState.Success -> {
                    _uiEvent.emit(LoginScreenUiEvent.Login.Success)
                }

                is UiState.Error -> {
                    _uiEvent.emit(LoginScreenUiEvent.Login.Fail)
                }

                is UiState.Fail -> {
                    _uiEvent.emit(LoginScreenUiEvent.Login.Fail)
                }
            }
        }
    }

    fun onClickLogin() {
        viewModelScope.launch {
            _uiEvent.emit(LoginScreenUiEvent.Login.Start)
        }
    }
}
