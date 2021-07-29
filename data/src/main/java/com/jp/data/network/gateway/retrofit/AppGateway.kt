package com.jp.data.network.gateway.retrofit

import com.jp.data.entity.sample.SampleEntity
import com.jp.data.network.gateway.IAppGateway
import com.jp.data.preferences.URLPreferenceManager
import com.jp.data.network.gateway.retrofit.service.IRestServices
import io.reactivex.Single
import retrofit2.Response

class AppGateway(private val mService: IRestServices, private val urlPreferenceManager: URLPreferenceManager) : IAppGateway {

    override suspend fun getSamples(): SampleEntity {
        return mService.getSamples(urlPreferenceManager.getURL() + "games")
    }

    override suspend fun getBoolean(): Response<Boolean> {
        Thread.sleep(5000)
        return Response.success(200,true)
    }
}