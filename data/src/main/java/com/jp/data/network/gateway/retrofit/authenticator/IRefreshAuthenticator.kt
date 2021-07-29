package com.jp.data.network.gateway.retrofit.authenticator

import io.reactivex.Single

interface IRefreshAuthenticator {
    fun userOauthRefreshedBearerToken(): String
    fun logOut()
}