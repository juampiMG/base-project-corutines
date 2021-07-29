package com.jp.data.preferences

import android.content.Context
import android.content.res.Configuration
import com.jp.data.utils.AdvancedPreferences
import java.util.*


class LocalePreferenceManager constructor(private val context: Context) {
    private val LOCALE_PREFERENCES = "locale_preferences"

    private val mAdvancedPreferences: AdvancedPreferences by lazy {
        AdvancedPreferences(context.getSharedPreferences(LOCALE_PREFERENCES, Context.MODE_PRIVATE))
    }


    fun saveLocale(lang: String) {
        mAdvancedPreferences.put(PREF_LANGUAGE_SELECTED, lang)
        mAdvancedPreferences.commit()
    }

    fun saveLocale(locale: Locale) {
        mAdvancedPreferences.put(PREF_LANGUAGE_SELECTED, locale.toString())
        mAdvancedPreferences.commit()
    }

    fun getLocale(): Locale {
        var savedLocale = mAdvancedPreferences[PREF_LANGUAGE_SELECTED, ""]
        if (savedLocale.isNullOrBlank())savedLocale = Locale.getDefault().toString()
        val values = savedLocale.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Locale(values[0])
    }

    private fun updateConfiguration(): Context {
        val locale = getLocale()
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration).apply {
            setLocale(locale)
        }
        return context.createConfigurationContext(config)
    }

    companion object {
        const val PREF_LANGUAGE_SELECTED = "pref_language_selected"

        var availableLangs: HashMap<String, String> = hashMapOf(
            ("български" to "bg"),
            ("English" to "en"),
            ("Español" to "es"),
            ("Français" to "fr"),
            ("Italiano" to "it"),
            ("עברית" to "iw"),
            ("Polski" to "pl"),
            ("Português" to "pt"))

        @JvmStatic
        fun setDefaultLocale(context: Context): Context {
            return LocalePreferenceManager(context).updateConfiguration()
        }


        @JvmStatic
        fun getSupportedLocales(): List<String> {
            val arrayLanguage = mutableListOf<String>()
            for (lang in availableLangs) {
                arrayLanguage.add(lang.key)
            }
            return arrayLanguage
        }

    }
}