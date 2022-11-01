package com.movingmaker.commentdiary.domain.model

import timber.log.Timber

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Fail(val message: String) : NetworkResult<Nothing>()

    //    object TokenExpired : NetworkResult<Nothing>()
    data class Exception(val errorType: ErrorType) :
        NetworkResult<Nothing>()
}

fun <T> NetworkResult<T>.toUiState(): UiState<T> {
    return when (this) {
        is NetworkResult.Success -> {
            if ((this.data as BaseResponse<*>).mapData { it } == null) {
                Timber.d("empty $this")
                UiState.Empty(this.data.message)
            } else {
                Timber.d("success $this.data")
                UiState.Success(this.data)
            }
        }
        is NetworkResult.Exception -> {
            Timber.d("error $this")
            UiState.Error(this.errorType.getErrorMessage())
        }
        is NetworkResult.Fail -> {
            Timber.d("fail $this")
            UiState.Fail(this.message)
        }
    }
}