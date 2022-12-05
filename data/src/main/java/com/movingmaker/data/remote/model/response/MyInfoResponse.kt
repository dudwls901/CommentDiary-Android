package com.movingmaker.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.MyInfo

data class MyInfoResponse(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "loginType")
    val loginType: String,
    @SerializedName(value = "temperature")
    val temperature: Double,
    @SerializedName(value = "pushYn")
    val pushYN: Char
) : RemoteModel {
    override fun toDomainModel() = MyInfo(
        email, loginType, temperature, pushYN
    )
}

fun MyInfo.toDataModel() = MyInfoResponse(
    email, loginType, temperature, pushYN
)
