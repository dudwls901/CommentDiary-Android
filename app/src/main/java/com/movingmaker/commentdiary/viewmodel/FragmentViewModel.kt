package com.movingmaker.commentdiary.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.repository.LogOutRepository
import com.movingmaker.commentdiary.model.repository.MyPageRepository
import kotlinx.coroutines.withContext
import retrofit2.Response

class FragmentViewModel (application: Application) : AndroidViewModel(application) {
    //api response
    private var _fragmentState = MutableLiveData<String>()
    private var _hasBottomNavi = MutableLiveData<Boolean>()
    private var _beforeFragment = MutableLiveData<String>()

    val fragmentState: LiveData<String>
        get() = _fragmentState

    val hasBottomNavi: LiveData<Boolean>
        get() = _hasBottomNavi

    val beforeFragment: LiveData<String>
        get() = _beforeFragment

    init{
        _fragmentState.value = "myDiary"
        _hasBottomNavi.value = true

    }

    fun setFragmentState(fragment: String){
        _fragmentState.value = fragment
    }

    fun setHasBottomNavi(bool: Boolean){
        _hasBottomNavi.value = bool
    }

    fun setBeforeFragment(fragment: String){
        _beforeFragment.value = fragment
    }
}