package com.autonomad.di

import com.autonomad.data.models.pref.PreferenceStorage
import com.autonomad.data.models.pref.repo.LocalStorage
import com.autonomad.data.models.pref.repo.LocalStorageImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { PreferenceStorage(androidContext()) }
    single { LocalStorageImpl(get()) as LocalStorage }
}