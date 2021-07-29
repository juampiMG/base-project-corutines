package com.jp.domain.interactor.impl

import com.jp.domain.interactor.IGetSampleUseCase
import com.jp.domain.model.SampleDomain
import com.jp.domain.repository.ISampleRepository
import javax.inject.Inject

class GetSampleUseCase
@Inject
constructor() : IGetSampleUseCase {

    @Inject
    internal lateinit var mSampleRepository: ISampleRepository

    override suspend fun execute(): SampleDomain {
        return mSampleRepository.getSamples()
    }
}