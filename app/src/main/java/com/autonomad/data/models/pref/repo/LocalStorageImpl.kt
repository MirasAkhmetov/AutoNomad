package com.autonomad.data.models.pref.repo

import com.autonomad.data.models.pref.PreferenceStorage

class LocalStorageImpl(
    private val preferenceStorage: PreferenceStorage
) : LocalStorage {
    override fun isPinLoginEnabled() = preferenceStorage.isPinEnabled

    override fun isFingerprintLoginEnabled() = preferenceStorage.isFingerprintEnabled

    override fun isPushEnabled() = preferenceStorage.isPushEnabled

    override fun disableFingerprint() {
        preferenceStorage.isFingerprintEnabled = false
    }

    override fun disablePin() {
        preferenceStorage.isPinEnabled = false
    }

    override fun disablePush() {
        preferenceStorage.isPushEnabled = false
    }

    override fun enableFingerprint() {
        preferenceStorage.isFingerprintEnabled = true
    }

    override fun enablePin() {
        preferenceStorage.isPinEnabled = true
    }

    override fun enablePush() {
        preferenceStorage.isPushEnabled = true
    }

    override fun clear() {
        preferenceStorage.clear()
    }
}