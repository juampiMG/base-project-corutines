package com.jp.app.injector.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.jp.app.BuildConfig
import com.jp.data.preferences.CredentialsPreferenceManager
import com.jp.data.preferences.LocalePreferenceManager
import com.jp.data.preferences.URLPreferenceManager
import com.jp.data.preferences.UserPreferenceManager
import com.jp.data.utils.AdvancedPreferences

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides application-wide dependencies.
 */
@InstallIn(SingletonComponent::class)
@Module
object PreferencesModule {

    @Provides
    @Singleton
    internal fun sharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    internal fun advancedPreferences(sharedPreferences: SharedPreferences): AdvancedPreferences {
        return AdvancedPreferences(sharedPreferences)
    }

    @Provides
    @Singleton
    internal fun userPreferenceManager(preferences: AdvancedPreferences): UserPreferenceManager {
        return UserPreferenceManager(preferences)
    }

    @Provides
    @Singleton
    internal fun credentialsPreferenceManager(@ApplicationContext context: Context, preferences: AdvancedPreferences): CredentialsPreferenceManager {
        return CredentialsPreferenceManager(context, preferences)
    }

    @Provides
    @Singleton
    internal fun localePreferenceManager(@ApplicationContext context: Context): LocalePreferenceManager {
        return LocalePreferenceManager(context)
    }

    @Provides
    @Singleton
    internal fun urlPreferenceManager(preferences: AdvancedPreferences): URLPreferenceManager {
        return URLPreferenceManager(preferences)
    }

}
