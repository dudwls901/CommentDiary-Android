package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.LogInModel

data class LogInRequest(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "password")
    val password: String,
    @SerializedName(value = "deviceToken")
    val deviceToken: String
) : RemoteModel {
    override fun toDomainModel() = LogInModel(
        email, password, deviceToken
    )
}

fun LogInModel.toDataModel() = LogInRequest(
    email, password, deviceToken
)
