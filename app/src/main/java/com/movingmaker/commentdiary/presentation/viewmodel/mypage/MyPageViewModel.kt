package com.movingmaker.commentdiary.presentation.viewmodel.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.common.CodaApplication
import com.movingmaker.commentdiary.common.util.DateConverter
import com.movingmaker.commentdiary.common.util.Event
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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
) : ViewModel() {

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _snackMessage = MutableLiveData<Event<String>>()
    val snackMessage: LiveData<Event<String>>
        get() = _snackMessage

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

    private fun setPushYN(yn: Char) {
        _pushYN.value = yn
    }

    fun signOut() = viewModelScope.launch {
        onLoading()
        with(signOutUseCase()) {
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    _snackMessage.value = Event("회원 탈퇴가 완료되었습니다.")
                    CodaApplication.getInstance().signOut()
                }
                is UiState.Error -> {
                    _snackMessage.value = Event(this.message)
                }
                is UiState.Fail -> {
                    _snackMessage.value = Event(this.message)
                }
            }
        }
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest) =
        viewModelScope.launch {
            onLoading()
            with(changePasswordUseCase(changePasswordRequest)) {
                Timber.d("result $this")
                offLoading()
                when (this) {
                    is UiState.Success -> {
                        _snackMessage.value = Event("비밀번호를 변경하였습니다.")
                    }
                    is UiState.Error -> {
                        _snackMessage.value = Event(this.message)
                    }
                    is UiState.Fail -> {
                        _snackMessage.value = Event(this.message)
                    }
                }
            }
        }

    fun logout() = viewModelScope.launch {
        onLoading()
        with(logOutUseCase()) {
            Timber.d("result $this")
            offLoading()
            when (this) {
                is UiState.Success -> {
                    CodaApplication.getInstance().logOut()
                }
                is UiState.Error -> {
                    _snackMessage.value = Event(this.message)
                }
                is UiState.Fail -> {
                    _snackMessage.value = Event(this.message)
                }
            }
        }
    }

    fun getMyPage() = viewModelScope.launch {
        onLoading()
        with(getMyPageUseCase()) {
            Timber.d("result $this")
            offLoading()
            when (this) {
                is UiState.Success -> {
                    setMyAccount(data.email)
                    setTemperature(data.temperature)
                    setPushYN(data.pushYN)
                    setLoginType(data.loginType)
                }
                is UiState.Error -> {
                    _snackMessage.value = Event(message)
                }
                is UiState.Fail -> {
                    _snackMessage.value = Event(message)
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
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    setCommentList(data)
                }
                is UiState.Error -> {
                    _snackMessage.value = Event(message)
                }
                is UiState.Fail -> {
                    _snackMessage.value = Event(message)
                }
            }
        }
    }

    fun setResponsePatchCommentPushState() = viewModelScope.launch {
        onLoading()
        with(patchCommentPushStateUseCase()) {
            offLoading()
            Timber.d("result $this")
            when (this) {
                is UiState.Success -> {
                    data["pushYn"]?.let{yn -> setPushYN(yn)}
                }
                is UiState.Error -> {
                    _snackMessage.value = Event(message)
                }
                is UiState.Fail -> {
                    _snackMessage.value = Event(message)
                }
            }
        }
    }

    private fun onLoading() {
        _loading.value = true
    }

    private fun offLoading() {
        _loading.value = false
    }

}