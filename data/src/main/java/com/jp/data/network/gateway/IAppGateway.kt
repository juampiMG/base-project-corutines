package com.jp.data.network.gateway

import com.jp.data.entity.sample.SampleEntity
import io.reactivex.Single
import retrofit2.Response


interface IAppGateway {
    suspend fun getSamples(): SampleEntity
    suspend fun getBoolean (): Response<Boolean>
}