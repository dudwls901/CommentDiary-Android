package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.SignUpModel
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val password: String,
    val checkPassword: String,
    val loginType: String
) : RemoteModel {
    override fun toDomainModel() = SignUpModel(
        email, password, checkPassword, loginType
    )
}

fun SignUpModel.toDataModel() = SignUpRequest(
    email, password, checkPassword, loginType
)