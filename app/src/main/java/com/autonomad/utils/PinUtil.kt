package com.autonomad.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

object PinUtil {
    private const val KEY_PIN = "pin_code_key"
    private const val KEY_SALT = "salt_key"
    private const val PREFERENCES = "pin_settings"
    private lateinit var preferences: SharedPreferences

    fun savePin(context: Context, pin: String) {
        val salt = generate()
        val bytes = pin.toByteArray()
        val encodedHash = Base64.encodeToString(hash(bytes.plus(salt)), 0)
        getPreferences(context).edit().putString(KEY_PIN, encodedHash).putString(KEY_SALT, Base64.encodeToString(salt, 0))
            .apply()
    }

    fun isPinValid(context: Context, pin: String): Boolean {
        val encodedSalt = getPreferences(context).getString(KEY_SALT, null)
        val encodedHash = getPreferences(context).getString(KEY_PIN, null)
        val salt = Base64.decode(encodedSalt, 0)
        val hash = Base64.decode(encodedHash, 0)
        val bytes = pin.toByteArray()
        val enteredPinHash = hash(bytes.plus(salt))
        return Arrays.equals(hash, enteredPinHash)
    }

    fun pinExists(context: Context) = getPreferences(context).contains(KEY_PIN)

    fun clear(context: Context) {
        getPreferences(context).edit().remove(KEY_PIN).remove(KEY_SALT).apply()
    }

    private fun generate(length: Int = 32): ByteArray {
        val bytes = ByteArray(length)
        SecureRandom().nextBytes(bytes)
        return bytes
    }

    private fun hash(input: ByteArray): ByteArray {
        val digest: MessageDigest = try {
            MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            MessageDigest.getInstance("SHA")
        }
        return digest.digest(input)
    }

    private fun getPreferences(context: Context): SharedPreferences {
        if (!::preferences.isInitialized) {
            preferences = context.getSharedPreferences(PREFERENCES, 0)
        }
        return preferences
    }
}