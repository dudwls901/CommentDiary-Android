package com.movingmaker.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)