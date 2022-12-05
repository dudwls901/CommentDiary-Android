package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.KakaoSignUpModel

data class KakaoSignUpRequest(
    @SerializedName("pushYn")
    val pushYN: Char
) : RemoteModel {
    override fun toDomainModel() = KakaoSignUpModel(
        pushYN
    )
}

fun KakaoSignUpModel.toDataModel() = KakaoSignUpRequest(
    pushYN
)
