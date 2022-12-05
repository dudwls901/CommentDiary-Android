package com.movingmaker.data.remote.model

interface RemoteModel {
    fun toDomainModel(): Any
}