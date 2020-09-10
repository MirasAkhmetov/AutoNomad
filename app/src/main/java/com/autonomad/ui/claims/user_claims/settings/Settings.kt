package com.autonomad.ui.claims.user_claims.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.autonomad.BuildConfig
import com.autonomad.R
import com.autonomad.ui.BottomNavMenu
import com.autonomad.ui.MainActivity
import com.autonomad.utils.Methods
import com.autonomad.utils.NavigationFragment
import kotlinx.android.synthetic.main.fragment_claims_user_settings.*

class Settings : NavigationFragment(R.layout.fragment_claims_user_settings) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        link_to_favourites.setOnClickListener {
            findNavController().navigate(R.id.action_claim_user_settings_to_favourites)
        }
        napisat.setOnClickListener {
            findNavController().navigate(R.id.action_claim_usser_settings_to_support)
        }
        link_to_specialist.setOnClickListener {
            Methods.setSpecialist(true)
            findNavController().navigate(R.id.action_claim_user_settings_to_claim_specialist_home)
            navigationViewModel.setMenu(BottomNavMenu.ClaimsSpecialistMenu)
        }
        ll_rate_app.setOnClickListener {
            val uri = Uri.parse("market://details?id=" + (activity as MainActivity).packageName)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                (activity as MainActivity).startActivity(intent)
            } catch (e: Exception) {
                (activity as MainActivity).startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + (activity as MainActivity).packageName)
                    )
                )
            }
        }
        ll_share_with_friends.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                val shareMessage = "${getString(R.string.share_text)} https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_text_choose_app)))
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}