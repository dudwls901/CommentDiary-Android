package com.movingmaker.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.movingmaker.presentation.util.event.Event

abstract class BaseViewModel : ViewModel() {

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _snackMessage = MutableLiveData<Event<String>>()
    val snackMessage: LiveData<Event<String>>
        get() = _snackMessage

    fun onLoading() {
        _loading.value = true
    }

    fun offLoading() {
        _loading.value = false
    }

    fun setMessage(message: String) {
        _snackMessage.value = Event(message)
    }

}