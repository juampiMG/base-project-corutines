package com.jp.data.network.gateway.retrofit.authenticator

import android.util.Log
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class RefreshAuthenticator(private val delegate: IRefreshAuthenticator) : Authenticator {
    private val mAuthorization = "Authorization"

    override fun authenticate(route: Route?, response: Response?): Request? {
        Log.d("RefreshAuthenticator", "Detected authentication error ${response?.code()} on ${response?.request()?.url()}")
        return if (response?.code() == 401) {
            reAuthenticateRequestUsingRefreshToken(response.request())
        } else {
            null
        }
    }

    @Synchronized
    // We synchronize this request, so that multiple concurrent failures
    // don't all try to use the same refresh token!
    // Attempting to fetch a new token...
    private fun reAuthenticateRequestUsingRefreshToken(staleRequest: Request?): Request? {
       // TODO add coroutine
        return try {
            rewriteRequest(staleRequest, delegate.userOauthRefreshedBearerToken())
        }
        catch (exception: Exception){
            //Failed to retrieve new token, unable to re-authenticate!
            delegate.logOut()
            null
        }

    }


    private fun rewriteRequest(staleRequest: Request?, authToken: String): Request? {
        return staleRequest?.newBuilder()?.header(mAuthorization, authToken)?.build()
    }


}