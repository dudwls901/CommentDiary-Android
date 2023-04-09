package com.movingmaker.data.remote.model.request

import com.movingmaker.domain.model.request.KakaoSignUpModel
import kotlinx.serialization.Serializable

@Serializable
data class KakaoSignUpRequest(
    val pushYn: Char
)

fun KakaoSignUpModel.toDataModel() = KakaoSignUpRequest(
    pushYN
)
