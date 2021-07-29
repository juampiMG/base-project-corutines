package com.jp.data.utils

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.Key
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

@TargetApi(Build.VERSION_CODES.M)
class EncryptionMUtil private constructor(): EncryptionInterface {

    companion object {

        @Volatile private var INSTANCE: EncryptionMUtil? = null

        fun getInstance(): EncryptionMUtil =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                        ?: EncryptionMUtil().also { INSTANCE = it }
                }

    }

    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
    private val fixedIV = generateIV()
    val IV_SEPARATOR = "]"

    override val AES_MODE: String
        get() = "AES/GCM/NoPadding"

    init {

        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
            keyGenerator.init(
                    KeyGenParameterSpec.Builder(KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false) // Android may force us to use randomized IV but it sometimes fails to retrieve IV https://proandroiddev.com/secure-data-in-android-initialization-vector-6ca1c659762c
                            .build())
            keyGenerator.generateKey()
        }
    }

    private fun generateIV(): ByteArray {
        val secureRandom = SecureRandom()
        val result = ByteArray(96 / 8)
        secureRandom.nextBytes(result)
        return result
    }

    private fun getSecretKey(): Key? {
        return keyStore.getKey(KEY_ALIAS, null)
    }

    override fun encrypt(input: String): String? {
        val c = Cipher.getInstance(AES_MODE)
        c.init(Cipher.ENCRYPT_MODE, getSecretKey(), GCMParameterSpec(128, fixedIV))
        var result = Base64.encodeToString(fixedIV, Base64.DEFAULT) + IV_SEPARATOR
        val encodedBytes = c.doFinal(input.toByteArray(charset = Charsets.UTF_8))
        result += Base64.encodeToString(encodedBytes, Base64.DEFAULT)
        return result
    }

    override fun decrypt(encrypted: String): String? {
        val split = encrypted.split(IV_SEPARATOR.toRegex())
        if (split.size != 2)
            throw IllegalArgumentException("Passed data is incorrect. There was no IV specified with it.")
        val iv = Base64.decode(split[0], Base64.DEFAULT)
        val encryptedBytes = Base64.decode(split[1], Base64.DEFAULT)
        val c = Cipher.getInstance(AES_MODE)
        c.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
        return try {
            String(c.doFinal(encryptedBytes), charset = Charsets.UTF_8)
        } catch ( e : Exception) {
            // If fails logout the user
            null
        }

    }
}