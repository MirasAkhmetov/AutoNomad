package com.autonomad.data.models.pref.repo

interface LocalStorage {
    fun isPinLoginEnabled(): Boolean
    fun isFingerprintLoginEnabled(): Boolean
    fun isPushEnabled(): Boolean

    fun disableFingerprint()
    fun disablePin()
    fun disablePush()

    fun enableFingerprint()
    fun enablePin()
    fun enablePush()

    fun clear()
}