package com.movingmaker.data.remote.model

interface RemoteResponse {
    fun toDomainModel(): Any
}