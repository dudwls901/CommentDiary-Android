package com.movingmaker.commentdiary.data.util

import com.movingmaker.commentdiary.common.util.Constant.ERROR_KEY
import com.movingmaker.commentdiary.common.util.Constant.MESSAGE_KEY
import com.movingmaker.commentdiary.domain.model.ErrorType
import com.movingmaker.commentdiary.domain.model.NetworkResult
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(callFunction: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = callFunction.invoke()
        Timber.d("result ${response.body()}")
        if(response.isSuccessful) {
            NetworkResult.Success(response.body()!!)
        }else{
            if(response.code()  in 400 until 500) {
                NetworkResult.Fail(getErrorMessage(response.errorBody()))
            }else{
                NetworkResult.Exception(ErrorType.SERVER_ERROR)
            }
        }
    } catch (e: Exception) {
        Timber.e("error $e")
        when (e) {
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
