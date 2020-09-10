package com.autonomad.ui.services

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.BottomNavMenu
import com.autonomad.utils.Methods
import com.autonomad.utils.NavigationFragment
import kotlinx.android.synthetic.main.item_services_page1.*

class ServicesPage1 : NavigationFragment(R.layout.item_services_page1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout_penalty.setOnClickListener {
            findNavController().navigate(R.id.action_services_to_penalty)
            navigationViewModel.setMenu(BottomNavMenu.PenaltiesMenu)
        }
        layout_parking.setOnClickListener {
            findNavController().navigate(R.id.action_services_to_parking)
            navigationViewModel.setMenu(BottomNavMenu.ParkingMenu)
        }
        layout_insurance.setOnClickListener {
            findNavController().navigate(R.id.action_services_to_insuranceHome)
            navigationViewModel.setMenu(BottomNavMenu.InsuranceMenu)
        }
        layout_check_auto.setOnClickListener {
            findNavController().navigate(R.id.action_services_to_checkAutoHome)
            navigationViewModel.setMenu(BottomNavMenu.CheckAutoMenu)
        }
        layout_store.setOnClickListener {
//            findNavController().navigate(R.id.action_services_to_shop_home)
//            navigationViewModel.setMenu(BottomNavMenu.ShopMenu)
        }
        layout_claims.setOnClickListener {
            if (Methods.isSpecialist()) {
                findNavController().navigate(R.id.action_services_to_claim_specialist_home)
                navigationViewModel.setMenu(BottomNavMenu.ClaimsSpecialistMenu)
            } else {
                findNavController().navigate(R.id.action_services_to_claim_user_home)
                navigationViewModel.setMenu(BottomNavMenu.ClaimsUserMenu)
            }
        }
    }
}