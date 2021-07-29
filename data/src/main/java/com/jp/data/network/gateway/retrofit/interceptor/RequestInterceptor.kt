package com.jp.data.network.gateway.retrofit.interceptor

import android.content.Context
import com.jp.data.utils.PackageUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RequestInterceptor(val context: Context) : Interceptor {

    private val mUserAgent = "User-Agent"
    private val mAccept = "Accept"
    private val mContentType = "Content-Type"
    private val mAppPlatform = "X-g4l-app-platform"
    private val mAppVersion = "X-g4l-app-version"

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(getRequestBuilder(chain).build())
    }

    private fun getRequestBuilder(chain: Interceptor.Chain): Request.Builder {
        val original = chain.request()
        val url = original.url().toString()

        return original.newBuilder()
                .header(mUserAgent, PackageUtil.getAppAgentInfo(context))
                .header(mAccept, "application/json")
                .header(mContentType, "application/json")
                .header(mAppPlatform, "android")
                .url(url)
                .method(original.method(), original.body())
    }

}