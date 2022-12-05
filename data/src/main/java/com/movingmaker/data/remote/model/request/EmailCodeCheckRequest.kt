package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.EmailCodeCheckModel

data class EmailCodeCheckRequest(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "code")
    val code: Int
) : RemoteModel {
    override fun toDomainModel() = EmailCodeCheckModel(
        email, code
    )
}

fun EmailCodeCheckModel.toDataModel() = EmailCodeCheckRequest(
    email, code
)