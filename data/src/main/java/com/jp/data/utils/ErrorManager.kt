package com.jp.data.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jp.data.entity.ErrorResponse
import com.jp.data.exception.AppException
import com.jp.data.network.gateway.retrofit.exception.RetrofitException
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

class ErrorManager(val managerCallback: ErrorManagerCallback) {


    interface ErrorManagerCallback {
        fun onErrorManager(response: Response<*>?, error: String?, code: Int?)
    }

    fun handleError(throwable: Throwable) {
        when (throwable) {
            is RetrofitException -> handleRetrofitException(throwable)
            is AppException -> handleAppException(throwable)
            else -> managerCallback.onErrorManager(null, null, -1)
        }
    }

    private fun handleRetrofitException(exception: RetrofitException) {
        val response = exception.response
        val cause = exception.cause
        var code = exception.response?.code()
        var message = parseErrorBody(exception.response?.errorBody())
        when (cause) {
            is ConnectException -> {
                managerCallback.onErrorManager(response, message, code)
            }
            is SocketTimeoutException -> {
                managerCallback.onErrorManager(response, message, code)
            }
            else -> try {
                val jsonError = exception.getErrorBodyAs(JSONObject::class.java)
                if (jsonError != null) {
                    try {
                        if (jsonError.has("code") && jsonError.has("description")) {
                            code = jsonError.getInt("code")
                            message = jsonError.getString("description")

                        }
                    } catch (e: JSONException) {
                        managerCallback.onErrorManager(response, message, code)
                    }

                } else {
                    managerCallback.onErrorManager(null, null, code)
                }
            } catch (e: IOException) {
                managerCallback.onErrorManager(response, message, code)
            }
        }
    }

    private fun parseErrorBody(body: ResponseBody?): String? {
        val type = object : TypeToken<ErrorResponse>() {}.type
        return try {
            val errorResponse = Gson().fromJson<ErrorResponse>(body?.string(), type)
            errorResponse.message
        } catch (e: Exception) {
            ""
        }

    }

    private fun handleAppException(exception: AppException) {
        val code = exception.mCode
        val message = exception.message
        managerCallback.onErrorManager(null, message, code)
    }
}