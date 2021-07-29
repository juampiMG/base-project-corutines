package com.jp.domain.interactor

import com.jp.domain.model.SampleDomain

interface IGetSampleUseCase {
    suspend fun execute(): SampleDomain
}