package com.autonomad.ui.main_page.home

interface MainPageClickHandler {
    fun onRefresh()
    fun onAddCar()
    fun onInternetErrorClick()
    fun onInsuranceClick()
    fun onPenaltiesClick()
    fun onInspectionClick()
    fun onServicesClick(serviceId: Int)
}