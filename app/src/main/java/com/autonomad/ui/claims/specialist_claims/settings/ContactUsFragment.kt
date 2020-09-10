package com.autonomad.ui.claims.specialist_claims.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.autonomad.R
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_claims_specialist_contact_us.*

class ContactUsFragment : Fragment(R.layout.fragment_claims_specialist_contact_us) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { activity?.onBackPressed() }

        btn_tg.setOnClickListener {
            val appName = "org.telegram.messenger"
            if (checkAppInstalled(appName)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/autonomad"))
                intent.setPackage(appName)
                startActivity(intent)
            } else {
                toast("Telegram не найден на данном устройстве")
            }
        }
        btn_wpp.setOnClickListener {
            val appName = "com.whatsapp"
            if (checkAppInstalled(appName)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/77002101234"))
                intent.setPackage(appName)
                startActivity(intent)
            } else {
                toast("Whatsapp не найден на данном устройстве")
            }
        }
    }

    private fun checkAppInstalled(appName: String): Boolean {
        val pm = requireContext().packageManager
        return try {
            pm.getApplicationInfo(appName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}