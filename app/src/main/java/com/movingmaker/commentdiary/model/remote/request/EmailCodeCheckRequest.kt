package com.movingmaker.commentdiary.model.remote.request

data class EmailCodeCheckRequest(
    val email: String,
    val code: Int
)