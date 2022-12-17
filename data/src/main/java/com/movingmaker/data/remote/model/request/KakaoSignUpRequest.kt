package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.KakaoSignUpModel
import kotlinx.serialization.Serializable

@Serializable
data class KakaoSignUpRequest(
    val pushYn: Char
) : RemoteModel {
    override fun toDomainModel() = KakaoSignUpModel(
        pushYn
    )
}

fun KakaoSignUpModel.toDataModel() = KakaoSignUpRequest(
    pushYN
)
