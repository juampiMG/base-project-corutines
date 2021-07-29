package com.jp.data.utils

import com.jp.data.BuildConfig


/**
 * Encryption interface using Android KeyStore and Cipher
 */
interface EncryptionInterface {

    private companion object {
        const val _ANDROID_KEY_STORE = "AndroidKeyStore"
        const val _KEY_ALIAS = BuildConfig.LIBRARY_PACKAGE_NAME + ".password"
        const val _ENCRYPTED_KEY = "android.password.encryptedKey"

    }

    var ANDROID_KEY_STORE
        get() = _ANDROID_KEY_STORE
        set(value) {}
    var KEY_ALIAS
        get() = _KEY_ALIAS
        set(value) {}
    var ENCRYPTED_KEY
        get() = _ENCRYPTED_KEY
        set(value) {}
    val AES_MODE: String

    /**
     * Performs encryption to input using secure Android KeyStore and Cipher
     *
     * See: https://medium.com/@ericfu/securely-storing-secrets-in-an-android-application-501f030ae5a3
     */
    fun encrypt(input: String): String?

    /**
     * Decrypts encrypted string using secure Android KeyStore and Cipher
     */
    fun decrypt(encrypted: String): String?
}