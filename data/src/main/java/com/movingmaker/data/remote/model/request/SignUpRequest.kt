package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.SignUpModel

data class SignUpRequest(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "password")
    val password: String,
    @SerializedName(value = "checkPassword")
    val checkPassword: String,
    @SerializedName(value = "loginType")
    val loginType: String
) : RemoteModel {
    override fun toDomainModel() = SignUpModel(
        email, password, checkPassword, loginType
    )
}

fun SignUpModel.toDataModel() = SignUpRequest(
    email, password, checkPassword, loginType
)