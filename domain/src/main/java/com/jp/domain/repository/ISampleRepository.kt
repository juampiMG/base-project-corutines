package com.jp.domain.repository

import com.jp.domain.model.SampleDomain
import retrofit2.Response

interface ISampleRepository {
    suspend fun getSamples(): SampleDomain
    suspend fun getBoolean(): Response<Boolean>
}