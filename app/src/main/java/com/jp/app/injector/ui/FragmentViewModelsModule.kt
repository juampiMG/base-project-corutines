package com.jp.app.injector.ui

import androidx.lifecycle.ViewModelProvider
import com.jp.app.common.viewModel.ViewModelProviderFactory
import com.jp.app.ui.basicSample.view.SampleFragment
import com.jp.app.ui.basicSample.viewModel.ISampleFragmentViewModel
import com.jp.app.ui.basicSample.viewModel.SampleFragmentViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

/**
 * Module that provide all the FragmentViewModels to the FragmentViews
 */

@InstallIn(FragmentComponent::class)
@Module
object FragmentViewModelsModule {
    @Provides
    fun provideViewModel(fragment: SampleFragment): ISampleFragmentViewModel {
        return ViewModelProvider(fragment, ViewModelProviderFactory(SampleFragmentViewModel()))
            .get(SampleFragmentViewModel::class.java)
    }
}