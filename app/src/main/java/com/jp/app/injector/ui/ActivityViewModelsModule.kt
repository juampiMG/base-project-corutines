package com.jp.app.injector.ui

import com.jp.app.ui.basicSample.activity.viewModel.ISampleActivityViewModel
import com.jp.app.ui.basicSample.activity.viewModel.SampleActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Module that Binds all the ViewModels to the Activity
 */

@InstallIn(ActivityComponent::class)
@Module
abstract class ActivityViewModelsModule {
    /**
     * Bind the current Activity View Model
     */
    @Binds
    internal abstract fun sampleActivityViewModel(viewModel: SampleActivityViewModel): ISampleActivityViewModel
}