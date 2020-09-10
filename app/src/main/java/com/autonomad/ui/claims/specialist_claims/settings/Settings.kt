package com.autonomad.ui.claims.specialist_claims.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.BottomNavMenu
import com.autonomad.utils.Methods
import com.autonomad.utils.NavigationFragment
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_claims_specialist_settings.*

class Settings : NavigationFragment(R.layout.fragment_claims_specialist_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        link_to_profile.setOnClickListener {
            findNavController().navigate(R.id.action_claim_specialist_settings_to_claim_specialist_profile)
        }

        link_to_statistics.setOnClickListener {
            findNavController().navigate(R.id.action_claim_specialist_settings_to_statistics)
        }

        link_to_support.setOnClickListener {
            findNavController().navigate(R.id.action_claim_specialist_settings_to_support)
        }

        link_to_tariffs.setOnClickListener {
            findNavController().navigate(R.id.action_claim_specialist_settings_to_tariffs)
        }

        link_to_client.setOnClickListener {
            Methods.setSpecialist(false)
            findNavController().navigate(R.id.action_claim_specialist_settings_to_claim_user_home)
            navigationViewModel.setMenu(BottomNavMenu.ClaimsUserMenu)
        }

        navigateBack(R.id.action_claim_specialist_settings_to_home)
    }
}