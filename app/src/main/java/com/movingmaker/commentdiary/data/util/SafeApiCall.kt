package com.movingmaker.commentdiary.data.util

import com.movingmaker.commentdiary.common.util.Constant.ERROR_KEY
import com.movingmaker.commentdiary.common.util.Constant.MESSAGE_KEY
import com.movingmaker.commentdiary.domain.model.NetworkResult
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

fun <T> Response<T>.safeApiCall(): NetworkResult<T> {
    return try {
        if (isSuccessful) {
            NetworkResult.Success(this.body()!!)
        } else {
            NetworkResult.Fail(getErrorMessage(this.errorBody()))
        }
    } catch (e: Exception) {
        when (e) {
            is HttpException -> {
                val body = e.response()?.errorBody()
                NetworkResult.Fail(getErrorMessage(body))
            }
            is SocketTimeoutException -> NetworkResult.Exception(ErrorType.TIMEOUT)
            is IOException -> NetworkResult.Exception(ErrorType.NETWORK)
            else -> NetworkResult.Exception(ErrorType.UNKNOWN)
        }
    }
}

fun getErrorMessage(responseBody: ResponseBody?): String {
    return try {
        val jsonObject = JSONObject(responseBody!!.string())
        when {
            jsonObject.has(MESSAGE_KEY) -> jsonObject.getString(MESSAGE_KEY)
            jsonObject.has(ERROR_KEY) -> jsonObject.getString(ERROR_KEY)
            else -> "Something wrong happened"
        }
    } catch (e: Exception) {
        "Something wrong happened"
    }
}
