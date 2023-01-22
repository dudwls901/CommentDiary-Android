package com.movingmaker.data.remote.model.request

import com.movingmaker.domain.model.request.ChangePasswordModel
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val password: String,
    val checkPassword: String
)

fun ChangePasswordModel.toDataModel() = ChangePasswordRequest(
    password, checkPassword
)
