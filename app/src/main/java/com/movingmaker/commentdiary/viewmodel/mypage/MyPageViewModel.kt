package com.movingmaker.commentdiary.viewmodel.mypage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.MyPageResponse
import com.movingmaker.commentdiary.model.repository.LogOutRepository
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import kotlinx.coroutines.withContext
import retrofit2.Response

class MyPageViewModel (application: Application) : AndroidViewModel(application) {

    private var _myAccount = MutableLiveData<String>()

    //api response
    private var _responseSignOut = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseLogOut = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseChangePassword = MutableLiveData<Response<IsSuccessResponse>>()
    private var _responseGetMyPage = MutableLiveData<Response<MyPageResponse>>()

    val myAccount: LiveData<String>
        get() = _myAccount

    val responseSignOut: LiveData<Response<IsSuccessResponse>>
        get() = _responseSignOut

    val responseChangePassword: LiveData<Response<IsSuccessResponse>>
        get() = _responseChangePassword

    val responseLogOut: LiveData<Response<IsSuccessResponse>>
        get() = _responseLogOut

    val responseGetMyPage: LiveData<Response<MyPageResponse>>
        get() = _responseGetMyPage

    init{

    }

    fun setMyAccount(email: String){
        _myAccount.value = email
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

}