package com.jp.domain.interactor

import com.jp.domain.model.SampleDomain
import retrofit2.Response

interface IGetSampleBooleanUseCase {
    suspend fun execute(): Response<Boolean>
}