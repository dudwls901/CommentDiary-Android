package com.movingmaker.domain.model.response

data class AuthTokens(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
    val isNewMember: Boolean?
)
