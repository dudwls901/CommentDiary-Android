package com.movingmaker.domain.model.response

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val result: T
)
