package com.movingmaker.domain.model.response

data class Login(
    val authTokens: AuthTokens,
    val userId: Long,
)