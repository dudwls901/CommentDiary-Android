package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.ChangePasswordModel

data class ChangePasswordRequest(
    @SerializedName(value = "password")
    val password: String,
    @SerializedName(value = "checkPassword")
    val checkPassword: String
) : RemoteModel {
    override fun toDomainModel() =
        ChangePasswordModel(
            password,
            checkPassword
        )
}

fun ChangePasswordModel.toDataModel() = ChangePasswordRequest(
    password, checkPassword
)
