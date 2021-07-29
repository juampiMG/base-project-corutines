package com.jp.app.injector.base

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.jp.app.helper.DialogHelper
import com.jp.app.helper.NavigationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object BaseActivityModule {

    @Provides
    internal fun activityExtras(activity: FragmentActivity): Bundle {
        return activity.intent.extras ?: run { Bundle() }
    }

    @Provides
    internal fun activityFragmentManager(activity: FragmentActivity): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    internal fun dialogHelper(
        activity: FragmentActivity,
        fragmentManager: FragmentManager
    ): DialogHelper {
        return DialogHelper(activity, fragmentManager)
    }

    @Provides
    internal fun navigationHelper(activity: FragmentActivity, extras: Bundle): NavigationHelper {
        return NavigationHelper(activity, extras)
    }

}