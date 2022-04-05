package com.movingmaker.commentdiary.viewmodel.mypage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.model.entity.Comment
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.CommentListResponse
import com.movingmaker.commentdiary.model.remote.response.CommentPushStateResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.MyPageResponse
import com.movingmaker.commentdiary.model.repository.LogOutRepository
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import com.movingmaker.commentdiary.util.DateConverter
import kotlinx.coroutines.withContext
import retrofit2.Response

class MyPageViewModel (application: Application) : AndroidViewModel(application) {

    private var _myAccount = MutableLiveData<String>()
    private var _temperature = MutableLiveData<Double>()
    private var _commentList = MutableLiveData<List<Comment>>()
    private var _selectedMonth = MutableLiveData<String>()
    private var _passwordCorrect = MutableLiveData<Boolean>()
    private var _passwordCheckCorrect = MutableLiveData<Boolean>()
    private var _canChangePassword = MutableLiveData<Boolean>()
    private var _pushYN = MutableLiveData<Char>()
    //api response
    private var _responseSignOut = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseLogOut = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseChangePassword = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseGetMyPage = MutableLiveData<Response<MyPageResponse>>()
    private var _responseGetMonthComment = MutableLiveData<Response<CommentListResponse>>()
    private var _responseGetAllComment = MutableLiveData<Response<CommentListResponse>>()
    private var _responsePatchCommentPushState = MutableLiveData<Response<CommentPushStateResponse>>()

    val myAccount: LiveData<String>
        get() = _myAccount

    val temperature: LiveData<Double>
        get() = _temperature

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

    val responseSignOut: LiveData<Response<IsSuccessResponse>>
        get() = _responseSignOut

    val responseChangePassword: LiveData<Response<IsSuccessResponse>>
        get() = _responseChangePassword

    val responseLogOut: LiveData<Response<IsSuccessResponse>>
        get() = _responseLogOut

    val responseGetMyPage: LiveData<Response<MyPageResponse>>
        get() = _responseGetMyPage

    val responseGetMonthComment: LiveData<Response<CommentListResponse>>
        get() = _responseGetMonthComment

    val responseGetAllComment: LiveData<Response<CommentListResponse>>
        get() = _responseGetAllComment

    val responsePatchCommentPushState: LiveData<Response<CommentPushStateResponse>>
        get() = _responsePatchCommentPushState

    init{
        _selectedMonth.value = DateConverter.ymFormat(DateConverter.getCodaToday())
        _passwordCorrect.value = true
        _passwordCheckCorrect.value = true
        _canChangePassword.value = false
    }

    fun setMyAccount(email: String){
        _myAccount.value = email
    }

    fun setTemperature(temp: Double){
        _temperature.value = temp
    }

    fun setCommentList(list: List<Comment>){
        _commentList.value = list
    }

    fun setSelectedMonth(date: String){
        _selectedMonth.value = date
    }

    fun setPasswordCorrect(isCorrect: Boolean){
        _passwordCorrect.value = isCorrect
        _canChangePassword.value = passwordCorrect.value!! && passwordCheckCorrect.value!!
    }

    fun setPasswordCheckCorrect(isCorrect: Boolean){
        _passwordCheckCorrect.value = isCorrect
        _canChangePassword.value = passwordCorrect.value!! && passwordCheckCorrect.value!!
    }

    fun setPushYN(yn: Char){
        _pushYN.value = yn
    }

    suspend fun setResponseSignOut() {
        withContext(viewModelScope.coroutineContext) {
            _responseSignOut.value = MyPageRepository.INSTANCE.signOut()
        }
    }

    suspend fun setResponseChangePassword(changePasswordRequest: ChangePasswordRequest) {
        withContext(viewModelScope.coroutineContext) {
            _responseChangePassword.value = MyPageRepository.INSTANCE.changePassword(changePasswordRequest)
        }
    }

    suspend fun setResponseLogOut(){
        withContext(viewModelScope.coroutineContext){
            _responseLogOut.value = LogOutRepository.INSTANCE.logOut()
        }
    }

    suspend fun setResponseGetMyPage(){
        withContext(viewModelScope.coroutineContext){
            _responseGetMyPage.value = MyPageRepository.INSTANCE.getMyPage()
        }
    }

    suspend fun setResponseGetAllComment(){
        withContext(viewModelScope.coroutineContext){
            _responseGetAllComment.value = MyPageRepository.INSTANCE.getAllComment()
        }
    }

    suspend fun setResponseGetMonthComment(date: String){
        withContext(viewModelScope.coroutineContext){
            _responseGetMonthComment.value = MyPageRepository.INSTANCE.getMonthComment(date)
        }
    }

    suspend fun setResponsePatchCommentPushState(){
        withContext(viewModelScope.coroutineContext){
            _responsePatchCommentPushState.value = MyPageRepository.INSTANCE.patchCommentPushState()
        }
    }

}