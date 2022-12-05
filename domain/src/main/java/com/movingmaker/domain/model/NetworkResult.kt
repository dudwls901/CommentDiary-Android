package com.movingmaker.domain.model

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
            UiState.Success(this.data)
        }
        is NetworkResult.Exception -> {
            UiState.Error(this.errorType.getErrorMessage())
        }
        is NetworkResult.Fail -> {
            UiState.Fail(this.message)
        }
    }
}