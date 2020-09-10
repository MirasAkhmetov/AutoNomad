package com.autonomad

import android.app.Application
import android.content.Context
import com.autonomad.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    companion object {
        lateinit var CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    dataModule,
                    networkModule,
                    repositoryModule,
                    viewModelModule,
                    serviceModule
                )
            )
        }
    }
}