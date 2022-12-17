package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.ChangePasswordModel
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val password: String,
    val checkPassword: String
) : RemoteModel {
    override fun toDomainModel() =
        ChangePasswordModel(
            password,
            checkPassword
        )
}
fun ChangePasswordModel.toDataModel() = ChangePasswordRequest(
    password, checkPassword
)
