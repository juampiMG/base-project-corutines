package com.jp.data.entity.mapper

import com.jp.data.entity.sample.SampleEntity
import com.jp.domain.model.SampleDomain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleEntityMapper
@Inject
constructor(): BaseModelDataMapper<SampleEntity, SampleDomain>() {

    override fun transform(source: SampleEntity): SampleDomain {
        val sampleDomain = SampleDomain()
        try {
            sampleDomain.id = source.id
        } catch (e: Exception) {
            e.stackTrace
        }

        return sampleDomain
    }

    override fun inverseTransform(source: SampleDomain): SampleEntity {
        val sampleEntity = SampleEntity()
        try {
        } catch (e: Exception) {
            e.stackTrace
        }

        return sampleEntity
    }

}