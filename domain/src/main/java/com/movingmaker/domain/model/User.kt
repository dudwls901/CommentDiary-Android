package com.movingmaker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val email: String,
    val permissions: Map<String,Boolean>? = null
)