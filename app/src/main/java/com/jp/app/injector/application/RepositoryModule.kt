package com.jp.app.injector.application

import com.jp.data.repository.SampleRepository
import com.jp.domain.repository.ISampleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    internal abstract fun sampleRepository(repository: SampleRepository): ISampleRepository
}
