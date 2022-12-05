package com.movingmaker.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.BaseResponse

@Suppress("UNCHECKED_CAST")
data class BaseResponse<T>(
    @SerializedName(value = "code")
    val code: Int,
    @SerializedName(value = "message")
    val message: String,
    @SerializedName(value = "result")
    val result: T
) {
    fun<R> toDomainModel(): BaseResponse<R> {
        return BaseResponse(
            code = code,
            message = message,
            result = when (val result = this.result) {
                is List<*> -> result.map { (it as RemoteModel).toDomainModel() } as R
                is RemoteModel -> (result as RemoteModel).toDomainModel()  as R
                else -> { result as R}
            }
        )
    }

    private fun toModel(): T = this.result

    fun <R> mapData(getData: (T) -> R): R {
        return getData(toModel())
    }
}
