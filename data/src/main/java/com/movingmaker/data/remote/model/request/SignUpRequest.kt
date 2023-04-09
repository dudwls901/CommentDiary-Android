package com.movingmaker.data.remote.model.request

import com.movingmaker.domain.model.request.SignUpModel
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val password: String,
    val checkPassword: String,
    val loginType: String
)

fun SignUpModel.toDataModel() = SignUpRequest(
    email, password, checkPassword, loginType
)