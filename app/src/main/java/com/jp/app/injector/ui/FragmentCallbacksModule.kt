package com.jp.app.injector.ui

import com.jp.app.ui.basicSample.activity.view.SampleActivity
import com.jp.app.ui.basicSample.view.SampleFragment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent

/**
 * Module that Binds all the FragmentCallbacks to the Actvities
 */
@InstallIn(FragmentComponent::class)
@Module
abstract class FragmentCallbacksModule {
    /**
     * Bind the Fragment Callback
     */
    @Binds
    internal abstract fun parentFragmentCallback(activity: SampleActivity): SampleFragment.FragmentCallback
}