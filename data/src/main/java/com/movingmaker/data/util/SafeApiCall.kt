package com.movingmaker.data.util

import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.ErrorResponse
import com.movingmaker.domain.model.ErrorType
import com.movingmaker.domain.model.NetworkResult
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(callFunction: suspend () -> Response<BaseResponse<T>>): NetworkResult<T> {
    return try {
        val response = callFunction.invoke()
        val responseBody = response.body()
        if (response.isSuccessful) {
            Timber.d("safeApiCall succes message ${responseBody?.message}")
            //result 없이 message만 내려주는 경우 Unit으로 변환
            @Suppress("UNCHECKED_CAST")
            NetworkResult.Success(responseBody!!.result?: Unit as T)
        } else {
            //http status
            if (response.code() in 400 until 500) {
                NetworkResult.Fail(getErrorMessage(response.errorBody()))
            } else {
                NetworkResult.Exception(null, ErrorType.SERVER_ERROR)
            }
        }
    } catch (e: Exception) {
        Timber.e("safeApiCall error $e")
        when (e) {
            is SocketTimeoutException -> NetworkResult.Exception(e.message, ErrorType.TIMEOUT)
            is IOException -> NetworkResult.Exception(e.message, ErrorType.NETWORK)
            else -> NetworkResult.Exception(e.message, ErrorType.UNKNOWN)
        }
    }
}

/**
 * json response 안의 code 혹은 message 접근
 * */
private fun getErrorMessage(responseBody: ResponseBody?): String {
    return try {
        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        json.decodeFromString(ErrorResponse.serializer(), responseBody!!.string()).message
    } catch (e: Exception) {
        Timber.e("$e")
        "Something wrong happened"
    }
}
