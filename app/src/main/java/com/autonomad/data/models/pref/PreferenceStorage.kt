package com.autonomad.data.models.pref

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.autonomad.data.models.pref.utils.BooleanPreference

class PreferenceStorage(context: Context) {
    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }
    var isFingerprintEnabled by BooleanPreference(prefs, IS_FINGERPRINT_ENABLED, true)
    var isPinEnabled by BooleanPreference(prefs, IS_PIN_ENABLED, true)
    var isPushEnabled by BooleanPreference(prefs, IS_PUSH_ENABLED, true)

    fun clear() {
        if (prefs.isInitialized()) prefs.value.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "com_autonomad_prefs"
        private const val IS_FINGERPRINT_ENABLED = "IS_FINGERPRINT_ENABLED"
        private const val IS_PIN_ENABLED = "IS_PIN_ENABLED"
        private const val IS_PUSH_ENABLED = "IS_PUSH_ENABLED"
    }
}