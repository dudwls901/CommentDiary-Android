package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteResponse
import com.movingmaker.domain.model.response.MyInfo
import kotlinx.serialization.Serializable

@Serializable
data class MyInfoResponse(
    val email: String,
    val loginType: String,
    val temperature: Double,
    val pushYn: Char
) : RemoteResponse {
    override fun toDomainModel() = MyInfo(
        email, loginType, temperature, pushYn
    )
}
