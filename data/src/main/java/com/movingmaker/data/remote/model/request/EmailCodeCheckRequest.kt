package com.movingmaker.data.remote.model.request

import com.movingmaker.domain.model.request.EmailCodeCheckModel
import kotlinx.serialization.Serializable

@Serializable
data class EmailCodeCheckRequest(
    val email: String,
    val code: Int
)

fun EmailCodeCheckModel.toDataModel() = EmailCodeCheckRequest(
    email, code
)