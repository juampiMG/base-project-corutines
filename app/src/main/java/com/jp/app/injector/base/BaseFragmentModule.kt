package com.jp.app.injector.base

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.jp.app.helper.DialogHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Named

/**
 * Provides base fragment dependencies. This must be included in all fragment modules, which must
 * provide a concrete implementation of [Fragment].
 */
@InstallIn(FragmentComponent::class)
@Module
object BaseFragmentModule {

    const val FRAGMENT = "BaseFragmentModule.fragment"
    const val CHILD_FRAGMENT_MANAGER = "BaseFragmentModule.childSupportFragment"
    const val FRAGMENT_DIALOG_HELPER = "BaseFragmentModule.dialogHelper"


    @Provides
    internal fun activity(activity: FragmentActivity): Activity {
        return activity
    }


    @Provides
    @Named(CHILD_FRAGMENT_MANAGER)
    internal fun childSupportFragment(@Named(FRAGMENT) fragment: Fragment): FragmentManager {
        return fragment.childFragmentManager
    }


    @Provides
    @Named(FRAGMENT_DIALOG_HELPER)
    internal fun dialogHelper(
        activity: FragmentActivity,
        @Named(CHILD_FRAGMENT_MANAGER) fragmentManager: FragmentManager
    ): DialogHelper {
        return DialogHelper(activity, fragmentManager)
    }

}
