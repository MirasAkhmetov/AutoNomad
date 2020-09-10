package com.autonomad.ui

import androidx.annotation.MenuRes
import com.autonomad.R

enum class BottomNavMenu(@MenuRes val menu: Int, val destinations: Set<Int>) {
    MainMenu(R.menu.main_bottom_nav_menu, setOf(R.id.main_page, R.id.services, R.id.profile)),
    PenaltiesMenu(R.menu.penalty_bottom_nav_menu, setOf(R.id.penalty, R.id.penalty_history)),
    ParkingMenu(R.menu.parking_bottom_nav_menu, setOf(R.id.parking_home, R.id.parking_history)),
    InsuranceMenu(R.menu.insurance_bottom_nav, setOf(R.id.insurance_home, R.id.insurance_policy)),
    CheckAutoMenu(R.menu.check_auto_bottom_nav_menu, setOf(R.id.check_auto_home, R.id.check_auto_history)),
    ShopMenu(R.menu.shop_home_menu, setOf(R.id.shop_home, R.id.shop_basket, R.id.shop_settings)),
    ClaimsUserMenu(
        R.menu.claim_user_bottom_nav_menu,
        setOf(R.id.claim_user_home, R.id.claim_user_claims, R.id.claim_user_settings)
    ),
    ClaimsSpecialistMenu(
        R.menu.claim_specialist_bottom_nav_menu,
        setOf(R.id.claim_specialist_home, R.id.claim_specialist_feedback, R.id.claim_specialist_settings)
    )
}