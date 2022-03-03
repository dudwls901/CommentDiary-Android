package com.movingmaker.commentdiary.model.remote.response

data class ErrorResponse (
    val status: Int,
    val code: String,
    val message: String
)