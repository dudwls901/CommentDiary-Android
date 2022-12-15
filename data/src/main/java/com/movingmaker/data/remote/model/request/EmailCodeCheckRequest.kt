package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.EmailCodeCheckModel
import kotlinx.serialization.Serializable

@Serializable
data class EmailCodeCheckRequest(
    val email: String,
    val code: Int
) : RemoteModel {
    override fun toDomainModel() = EmailCodeCheckModel(
        email, code
    )
}

fun EmailCodeCheckModel.toDataModel() = EmailCodeCheckRequest(
    email, code
)