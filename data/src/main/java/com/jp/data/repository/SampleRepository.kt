package com.jp.data.repository

import com.jp.data.entity.mapper.SampleEntityMapper
import com.jp.data.network.gateway.IAppGateway
import com.jp.domain.model.SampleDomain
import com.jp.domain.repository.ISampleRepository
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class SampleRepository
@Inject
constructor(private val mGateway: IAppGateway) : ISampleRepository {
    @Inject
    internal lateinit var mSampleEntityMapper: SampleEntityMapper

    override suspend fun getSamples(): SampleDomain {
        return mSampleEntityMapper.transform(mGateway.getSamples())
    }

    override suspend fun getBoolean(): Response<Boolean> {
        return mGateway.getBoolean()
    }
}