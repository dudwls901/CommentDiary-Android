package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteResponse
import kotlinx.serialization.Serializable

@Serializable
@Suppress("UNCHECKED_CAST")
data class BaseResponse<T>(
    val message: String,
    val result: T? = null
) {
    fun <R> toDomainModel(): BaseResponse<R> {
        return BaseResponse(
            message = message,
            result = when (val result = this.result) {
                is List<*> -> result.map { (it as RemoteResponse).toDomainModel() } as R
                is RemoteResponse -> result.toDomainModel() as R
                else -> {
                    result as R
                }
            }
        )
    }
}
