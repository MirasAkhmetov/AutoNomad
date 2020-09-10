package com.autonomad.ui.main_page.home

interface BottomDialogListener {
    fun onNewClick(item: MainPageItem)
    fun onItemClick(item: MainPageItem, id: Int)
}

enum class MainPageItem { INSURANCE, PENALTIES, INSPECTION }