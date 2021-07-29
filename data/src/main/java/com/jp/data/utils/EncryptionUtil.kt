package com.jp.data.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class EncryptionUtil private constructor(context: Context, private val mAdvancedPreferences: AdvancedPreferences) :
    EncryptionInterface {

    companion object {

        @Volatile
        private var INSTANCE: EncryptionUtil? = null

        fun getInstance(context: Context, advancedPreferences: AdvancedPreferences): EncryptionUtil =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: EncryptionUtil(context, advancedPreferences).also { INSTANCE = it }
            }

    }

    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)

    override val AES_MODE: String
        get() = "AES/ECB/PKCS7Padding"

    init {

        keyStore.load(null)
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            try {
                createKey(context, KEY_ALIAS)
            } catch (e: NoSuchProviderException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: InvalidAlgorithmParameterException) {
                e.printStackTrace()
            }

        }

        var encryptedKeyB64: String? = mAdvancedPreferences.get(ENCRYPTED_KEY, String())
        if (encryptedKeyB64.isNullOrEmpty()) {
            val key = ByteArray(16)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(key)
            val encryptedKey = rsaEncrypt(key)
            encryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT)
            mAdvancedPreferences.put(ENCRYPTED_KEY, encryptedKeyB64)
        }
    }

    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class)
    private fun createKey(context: Context, alias: String) {

        // We need to set start and end date because if not it throws and exception
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 30)
        val spec = KeyPairGeneratorSpec.Builder(context)
            .setAlias(alias)
            .setSubject(X500Principal("CN=Andjoy"))
            .setSerialNumber(BigInteger.TEN)
            .setStartDate(start.time)
            .setEndDate(end.time)
            .build()

        val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
        generator.initialize(spec)

        generator.generateKeyPair()
    }

    private fun rsaEncrypt(secret: ByteArray): ByteArray {

        val privateKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        // Encrypt the text
        val inputCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL")
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(secret)
        cipherOutputStream.close()

        return outputStream.toByteArray()
    }

    private fun rsaDecrypt(encrypted: ByteArray): ByteArray {

        val privateKeyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        val output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL")
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
        val cipherInputStream = CipherInputStream(
            ByteArrayInputStream(encrypted), output
        )
        val values = ArrayList<Byte>()
        var nextByte = cipherInputStream.read()
        while (nextByte != -1) {
            values.add(nextByte.toByte())
            nextByte = cipherInputStream.read()
        }

        val bytes = ByteArray(values.size)
        for (i in bytes.indices) {
            bytes[i] = values[i]
        }
        return bytes
    }

    private fun getSecretKey(): Key? {
        val encryptedKeyB64 = mAdvancedPreferences.get(ENCRYPTED_KEY, String())
        // need to check null, omitted here
        val encryptedKey = Base64.decode(encryptedKeyB64, Base64.DEFAULT)
        return SecretKeySpec(rsaDecrypt(encryptedKey), "AES")
    }

    override fun encrypt(input: String): String? {
        val inputBytes = input.toByteArray(Charsets.UTF_8)
        val c = Cipher.getInstance(AES_MODE, "BC")
        c.init(Cipher.ENCRYPT_MODE, getSecretKey())
        return Base64.encodeToString(c.doFinal(inputBytes), Base64.DEFAULT)
    }

    override fun decrypt(encrypted: String): String? {
        val c = Cipher.getInstance(AES_MODE, "BC")
        c.init(Cipher.DECRYPT_MODE, getSecretKey())
        val encryptedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        val decryptedBytes = c.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}