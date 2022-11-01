package com.movingmaker.commentdiary.domain.model

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Empty(val message: String) : UiState<Nothing>()
    data class Fail(val message: String) : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
}

fun <T> UiState<T>.toModel(): T {
    return (this as UiState.Success).data
}

private fun <R> changeUiState(replaceData: R): UiState<R> {
    return when (replaceData) {
        is String -> UiState.Empty(replaceData)
        else -> UiState.Success(replaceData)
    }
}

//success 분리
suspend fun <T, R> UiState<T>.mapUiState(getData: suspend (T) -> R): UiState<R> {
    return when (this) {
        is UiState.Success -> changeUiState(getData(toModel()))
        is UiState.Fail -> this
        is UiState.Error -> this
        is UiState.Empty -> this
        is UiState.Loading -> this
    }
}