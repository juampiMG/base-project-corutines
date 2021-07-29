package com.jp.data.preferences

import android.content.Context
import android.os.Build
import com.jp.data.utils.AdvancedPreferences
import com.jp.data.utils.EncryptionInterface
import com.jp.data.utils.EncryptionMUtil
import com.jp.data.utils.EncryptionUtil

class CredentialsPreferenceManager(val context: Context, private val mAdvancedPreferences: AdvancedPreferences) {

    fun removePassword() {
        setUserPassword(String())
    }

    fun getUserPassword(): String? {
        val encrypted = mAdvancedPreferences[PREF_USER_PASSWORD, String()]
        return if (encrypted == null || encrypted.isEmpty()) {
            null
        } else {
            getEncryptionInterface().decrypt(encrypted)
        }
    }

    fun setUserPassword(pwd: String?) {
        pwd?.let {
            getEncryptionInterface().encrypt(pwd)?.let { encrypted ->
                mAdvancedPreferences.put(PREF_USER_PASSWORD, encrypted)
                mAdvancedPreferences.commit()
            }

        }
    }

    fun removeToken() {
        setToken(String())
    }

    fun getToken(): String? {
        return mAdvancedPreferences.get(PREF_USER_TOKEN, String::class.java, "")
    }

    fun setToken(token: String?) {
        token?.let {
            mAdvancedPreferences.put(PREF_USER_TOKEN, token)
            mAdvancedPreferences.commit()
        }
    }


    private fun getEncryptionInterface(): EncryptionInterface {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            EncryptionUtil.getInstance(context, mAdvancedPreferences)
        } else {
            EncryptionMUtil.getInstance()
        }
    }

    companion object {
        private const val PREF_USER_TOKEN = "pref_user_token"
        private const val PREF_USER_PASSWORD = "pref_user_password"
    }

}