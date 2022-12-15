package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.KakaoLoginModel
import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequest(
    val loginType: String,
    val accessToken: String,
    val deviceToken: String
) : RemoteModel {
    override fun toDomainModel() = KakaoLoginModel(
        loginType, accessToken, deviceToken
    )
}

fun KakaoLoginModel.toDataModel() = KakaoLoginRequest(
    loginType, accessToken, deviceToken
)