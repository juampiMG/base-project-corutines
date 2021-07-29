package com.jp.app.injector.application

import com.jp.domain.interactor.IGetSampleBooleanUseCase
import com.jp.domain.interactor.IGetSampleUseCase
import com.jp.domain.interactor.impl.GetSampleBooleanUseCase
import com.jp.domain.interactor.impl.GetSampleUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class UseCaseModule {
    @Binds
    @Singleton
    internal abstract fun getSampleUseCase(useCase: GetSampleUseCase): IGetSampleUseCase

    @Binds
    @Singleton
    internal abstract fun getSampleBooleanUseCase(useCase: GetSampleBooleanUseCase): IGetSampleBooleanUseCase
}
