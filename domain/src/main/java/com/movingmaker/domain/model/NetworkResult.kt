package com.movingmaker.domain.model

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Fail(val message: String) : NetworkResult<Nothing>()
    data class Exception(val message: String?, val errorType: ErrorType) : NetworkResult<Nothing>()
}

fun <T> NetworkResult<T>.toUiState(): UiState<T> {
    return when (this) {
        is NetworkResult.Success -> {
            UiState.Success(data)
        }

        is NetworkResult.Exception -> {
            UiState.Error(message ?: errorType.getErrorMessage())
        }

        is NetworkResult.Fail -> {
            UiState.Fail(message)
        }
    }
}