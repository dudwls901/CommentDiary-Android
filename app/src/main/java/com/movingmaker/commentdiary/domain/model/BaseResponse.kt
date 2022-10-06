package com.movingmaker.commentdiary.domain.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName(value = "code")
    val code: Int,
    @SerializedName(value = "message")
    val message: String,
    @SerializedName(value = "result")
    val result: T
){
    private fun toModel(): T = this.result

    fun <R> mapData(getData: (T) -> R): R{
        return getData(toModel())
    }
}
