package com.jp.data.network.gateway.retrofit.interceptor

import android.content.Context
import com.jp.data.preferences.CredentialsPreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*


class RequestAuthInterceptor(val context: Context, val mCredentialsPreferenceManager: CredentialsPreferenceManager) : Interceptor {

    private val mUserAgent = "User-Agent"
    private val mAcceptLanguage = "Accept-Language"
    private val mAuthorization = "Authorization"
    private val mAppPlatform = "X-g4l-app-platform"
    private val mAppVersion = "X-g4l-app-version"

    override fun intercept(chain: Interceptor.Chain): Response {

        return chain.proceed(getRequestBuilder(chain).build())
    }

    private fun getRequestBuilder(chain: Interceptor.Chain): Request.Builder {
        val original = chain.request()
        val url = original.url().toString()

        return original.newBuilder()
                .header(mAcceptLanguage, Locale.getDefault().toString())
                .header(mAuthorization, getAuthorization())
                .header(mAppPlatform, "android")
                .url(url)
                .method(original.method(), original.body())
    }


    private fun getAuthorization(): String {
//        val token = mCredentialsPreferenceManager.getToken()
//        return if (token.isNullOrEmpty()) ""
//        else "Bearer $token"
        return ""

    }
}