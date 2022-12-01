package com.movingmaker.commentdiary.presentation.viewmodel.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.domain.model.UiState
import com.movingmaker.commentdiary.domain.usecase.ChangePasswordUseCase
import com.movingmaker.commentdiary.domain.usecase.GetAllCommentUseCase
import com.movingmaker.commentdiary.domain.usecase.GetMonthCommentUseCase
import com.movingmaker.commentdiary.domain.usecase.GetMyPageUseCase
import com.movingmaker.commentdiary.domain.usecase.LogOutUseCase
import com.movingmaker.commentdiary.domain.usecase.PatchCommentPushStateUseCase
import com.movingmaker.commentdiary.domain.usecase.SignOutUseCase
import com.movingmaker.commentdiary.presentation.CodaApplication
import com.movingmaker.commentdiary.presentation.base.BaseViewModel
import com.movingmaker.commentdiary.presentation.util.DateConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val getMyPageUseCase: GetMyPageUseCase,
    private val getAllCommentUseCase: GetAllCommentUseCase,
    private val getMonthCommentUseCase: GetMonthCommentUseCase,
    private val patchCommentPushStateUseCase: PatchCommentPushStateUseCase
) : BaseViewModel() {

    private var _isPasswordChanged = MutableLiveData<Boolean>()
    val isPasswordChanged: LiveData<Boolean>
        get() = _isPasswordChanged

    private var _myAccount = MutableLiveData<String>()
    val myAccount: LiveData<String>
        get() = _myAccount

    private var _temperature = MutableLiveData<Double>()
    val temperature: LiveData<Double>
        get() = _temperature

    private var _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    private var _loginType = MutableLiveData<String>()
    val loginType: LiveData<String>
        get() = _loginType

    private var _selectedMonth = MutableLiveData<String>().apply {
        value = DateConverter.ymFormat(
            DateConverter.getCodaToday()
        )
    }
    val selectedMonth: LiveData<String>
        get() = _selectedMonth

    var password = MutableLiveData<String>().apply { value = "" }
    var passwordCheck = MutableLiveData<String>().apply { value = "" }

    private var _passwordCorrect = MutableLiveData<Boolean>().apply { value = true }
    val passwordCorrect: LiveData<Boolean>
        get() = _passwordCorrect

    private var _passwordCheckCorrect = MutableLiveData<Boolean>().apply { value = true }
    val passwordCheckCorrect: LiveData<Boolean>
        get() = _passwordCheckCorrect

    private var _canChangePassword = MutableLiveData<Boolean>()
    val canChangePassword: LiveData<Boolean>
        get() = _canChangePassword

    private var _pushYN = MutableLiveData<Char>()
    val pushYN: LiveData<Char>
        get() = _pushYN

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
    }

    fun setSelectedMonth(date: String) {
        _selectedMonth.value = date
    }

    fun validatePassword() {
        var hasLetter = false
        var hasNum = false
        var hasSign = false
        for (ch in password.value!!) {
            when {
                ch.isLetter() -> hasLetter = true
                ch.isDigit() -> hasNum = true
                else -> hasSign = true
            }
        }
        _passwordCorrect.value =
            !(password.value!!.isNotEmpty() && (password.value!!.length !in 8..16 || !hasLetter || !hasNum || !hasSign))
        _passwordCheckCorrect.value =
            !(passwordCheck.value!!.isNotEmpty() && (password.value!! != passwordCheck.value!!))
        setCanChangePassword()
    }

    fun validatedPasswordCheck() {
        _passwordCheckCorrect.value = !(passwordCheck.value!!.isNotEmpty() &&
                (password.value != passwordCheck.value))
        setCanChangePassword()
    }

    private fun setCanChangePassword() {
        _canChangePassword.value = passwordCorrect.value!! && passwordCheckCorrect.value!!
    }

    private fun setPushYN(yn: Char) {
        _pushYN.value = yn
    }

    fun signOut() = viewModelScope.launch {
        onLoading()
        with(signOutUseCase()) {
            when (this) {
                is UiState.Success -> {
                    setMessage("회원 탈퇴가 완료되었습니다.")
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
    }

    fun changePassword() =
        viewModelScope.launch {
            onLoading()
            with(
                changePasswordUseCase(
                    ChangePasswordRequest(
                        password.value!!,
                        passwordCheck.value!!
                    )
                )
            ) {
                offLoading()
                when (this) {
                    is UiState.Success -> {
                        setMessage("비밀번호를 변경하였습니다.")
                    }
                    is UiState.Error -> {
                        setMessage(message)
                    }
                    is UiState.Fail -> {
                        setMessage(message)
                    }
                }
            }
        }

    fun logout() = viewModelScope.launch {
        onLoading()
        with(logOutUseCase()) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    CodaApplication.getInstance().logOut()
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
    }

    fun getMyPage() = viewModelScope.launch {
        onLoading()
        with(getMyPageUseCase()) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    setMyAccount(data.email)
                    setTemperature(data.temperature)
                    setPushYN(data.pushYN)
                    setLoginType(data.loginType)
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
    }

    fun getResponseCommentList(date: String) = viewModelScope.launch {
        onLoading()
        val response = if (date == "all") {
            getAllCommentUseCase()
        } else {
            getMonthCommentUseCase(date)
        }
        with(response) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    setCommentList(data)
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
    }

    fun setResponsePatchCommentPushState() = viewModelScope.launch {
        onLoading()
        with(patchCommentPushStateUseCase()) {
            offLoading()
            when (this) {
                is UiState.Success -> {
                    data["pushYn"]?.let { yn -> setPushYN(yn) }
                }
                is UiState.Error -> {
                    setMessage(message)
                }
                is UiState.Fail -> {
                    setMessage(message)
                }
            }
        }
    }
}