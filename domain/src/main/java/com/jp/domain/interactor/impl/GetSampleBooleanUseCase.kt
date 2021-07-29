package com.jp.domain.interactor.impl

import com.jp.domain.interactor.IGetSampleBooleanUseCase
import com.jp.domain.interactor.IGetSampleUseCase
import com.jp.domain.model.SampleDomain
import com.jp.domain.repository.ISampleRepository
import retrofit2.Response
import javax.inject.Inject

class GetSampleBooleanUseCase
@Inject
constructor() : IGetSampleBooleanUseCase {

    @Inject
    internal lateinit var mSampleRepository: ISampleRepository

    override suspend fun execute(): Response<Boolean> {
        return mSampleRepository.getBoolean()
    }
}