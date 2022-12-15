package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.LogInModel
import kotlinx.serialization.Serializable

@Serializable
data class LogInRequest(
    val email: String,
    val password: String,
    val deviceToken: String
) : RemoteModel {
    override fun toDomainModel() = LogInModel(
        email, password, deviceToken
    )
}

fun LogInModel.toDataModel() = LogInRequest(
    email, password, deviceToken
)
