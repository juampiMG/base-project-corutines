package com.jp.data.network.gateway.retrofit.service

import com.jp.data.entity.sample.SampleEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Services without authentication at the header
 */
interface IRestServices {
    @GET
    suspend fun getSamples(@Url url: String): SampleEntity
}