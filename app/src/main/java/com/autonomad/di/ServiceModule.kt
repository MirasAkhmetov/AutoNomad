package com.autonomad.di

import com.autonomad.utils.*
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val serviceModule = module {
    single { ApiGarageF.apiService }
    single { ApiCapsSingle.apiService }
    single { ApiMeteoCurrencyFactory.apiService }
    single { ApiChatFactory.apiService }
    factory { (userId: Int) ->
        ChatSocketFactory(application = androidApplication(), lifecycle = get(), userId = userId).socketService
    }

    single { LifecycleObservers(LifecycleRegistry(500L)) }
}
