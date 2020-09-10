package com.autonomad.di

import com.autonomad.data.repo.*
import com.autonomad.data.repo.chat.ChatRepo
import com.autonomad.data.repo.chat.ChatRepoImpl
import com.autonomad.ui.check_auto.repo.CheckAutoHistoryRepo
import com.autonomad.ui.check_auto.repo.CheckAutoHistoryRepoImpl
import com.autonomad.ui.insurance.repo.InsuranceRepo
import com.autonomad.ui.insurance.repo.InsuranceRepoImpl
import com.autonomad.ui.shop.repo.ProductRepo
import com.autonomad.ui.shop.repo.ProductRepoImpl
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single { InsuranceRepoImpl() as InsuranceRepo }
    single { CheckAutoHistoryRepoImpl() as CheckAutoHistoryRepo }
    single<CapsRepo> { CapsRepoImpl(apiService = get(), dispatcher = get(named(IO))) }
    single<PenaltiesRepo> { PenaltiesRepoImpl(apiService = get(), dispatcher = get(named(IO))) }
    single<GarageDriverRepo> { GarageDriverRepoImpl(apiService = get(), dispatcher = get(named(IO))) }
    single<ChatRepo> { ChatRepoImpl(capsApi = get(), chatApi = get(), dispatcher = get(named(IO))) }

    single(named(IO)) { Dispatchers.IO }
    single(named(MAIN)) { Dispatchers.Main }


    single { ProductRepoImpl(get()) as ProductRepo }
}

private const val IO = "dispatcherIo"
private const val MAIN = "dispatcherMain"