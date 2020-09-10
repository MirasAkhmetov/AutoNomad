package com.autonomad.ui

import androidx.lifecycle.LiveData

interface LoginListener {
    fun onLogin()
    fun onLogout(): LiveData<String>
}