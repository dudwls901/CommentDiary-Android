package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.KakaoLoginModel

data class KakaoLoginRequest(
    @SerializedName(value = "loginType")
    val loginType: String,
    @SerializedName(value = "accessToken")
    val accessToken: String,
    @SerializedName(value = "deviceToken")
    val deviceToken: String
) : RemoteModel {
    override fun toDomainModel() = KakaoLoginModel(
        loginType, accessToken, deviceToken
    )
}

fun KakaoLoginModel.toDataModel() = KakaoLoginRequest(
    loginType, accessToken, deviceToken
)