package com.movingmaker.commentdiary.viewmodel.mypage

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import kotlinx.coroutines.withContext
import retrofit2.Response

class MyPageViewModel (application: Application) : AndroidViewModel(application) {
    //api response
    private var _responseSignOut = MutableLiveData<Response<IsSuccessResponse>>()

    val responseSignOut: LiveData<Response<IsSuccessResponse>>
        get() = _responseSignOut

    init{

    }

    suspend fun setResponseSignOut() {
        withContext(viewModelScope.coroutineContext) {
            _responseSignOut.value = MyPageRepository.INSTANCE.signOut()
        }
    }
}