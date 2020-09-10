package com.autonomad.di

import com.autonomad.ui.chats.chat.ChatViewModel
import com.autonomad.ui.check_auto.history.CheckAutoHistoryViewModel
import com.autonomad.ui.check_auto.home.CheckAutoViewModel
import com.autonomad.ui.check_auto.receipt.DetailReceiptViewModel
import com.autonomad.ui.check_auto.report.ReportViewModel
import com.autonomad.ui.insurance.insurance_home.InsuranceHomeViewModel
import com.autonomad.ui.main_page.home.MainPageViewModel
import com.autonomad.ui.penalty.penalties_of_person.PenaltiesOfPersonViewModel
import com.autonomad.ui.profile.avto_profile.CarsPageDetailViewModel
import com.autonomad.ui.profile.driver_profile.DriverPageDetailViewModel
import com.autonomad.ui.shop.home.ShopHomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { InsuranceHomeViewModel(get()) }
    viewModel { CheckAutoViewModel(get()) }
    viewModel { CheckAutoHistoryViewModel(get()) }
    viewModel { DetailReceiptViewModel(get()) }
    viewModel { ReportViewModel(get()) }
    viewModel { MainPageViewModel(apiCaps = get(), apiMeteoCurrency = get(), apiGarage = get()) }
    viewModel { (uin: String) -> PenaltiesOfPersonViewModel(uin = uin, repo = get()) }
    viewModel { (carId: Int) -> CarsPageDetailViewModel(carId = carId, apiService = get()) }
    viewModel { (driverId: Int, uin: String) -> DriverPageDetailViewModel(driverId, uin, get()) }

    viewModel { ShopHomeViewModel(get()) }

    viewModel { (chatId: Int) -> ChatViewModel(repo = get(), chatId = chatId) }
}