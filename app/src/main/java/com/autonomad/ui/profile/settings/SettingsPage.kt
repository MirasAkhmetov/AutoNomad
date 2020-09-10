package com.autonomad.ui.profile.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.pref.repo.LocalStorage
import com.autonomad.ui.MainActivity
import com.autonomad.ui.login.pin_code_page.PinCodePage
import com.autonomad.ui.login.login_page.LoginFragment
import com.autonomad.utils.Methods
import com.autonomad.utils.PinUtil
import com.autonomad.utils.navigateBack
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import org.koin.android.ext.android.inject

class SettingsPage : Fragment(R.layout.fragment_profile_settings) {

    private val localStorage: LocalStorage by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener {
            findNavController().navigate(R.id.action_global_profile)
        }

        material1.setOnClickListener {
            findNavController().navigate(R.id.action_sett_bank)
        }
        material2.setOnClickListener {
            findNavController().navigate(R.id.action_sett_documsett)
        }
        material_nap.setOnClickListener {
            findNavController().navigate(R.id.action_sett_supp)
        }
        navigateBack(R.id.action_global_profile)
        exit.setOnClickListener {
            showSignOutAlertDialog()
        }

        card_addresses.setOnClickListener {
            findNavController().navigate(R.id.action_to_settings_address)
        }

        sharee.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=com.autonomad"
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        changepassword_material.setOnClickListener {
            findNavController().navigate(R.id.action_sett_changepass)
        }
        changepincode_material.setOnClickListener {
            val flag =
                if (PinUtil.pinExists(requireContext())) PinCodePage.FLAG_UPDATE_PIN else PinCodePage.FLAG_SET_PIN
            findNavController().navigate(
                R.id.action_global_pinCodePage,
                bundleOf(PinCodePage.FLAG to flag)
            )
        }
        val biometricManager = BiometricManager.from(context as Context)
        when (biometricManager.canAuthenticate()) {

            BiometricManager.BIOMETRIC_SUCCESS -> {
                //biometricPrompt.authenticate(promptInfo)
                otpechatka.visibility = View.VISIBLE
            }
            else -> {
                otpechatka.visibility = View.GONE
            }
        }


        push_sw.isChecked = localStorage.isPushEnabled()
        pin_sw.isChecked = localStorage.isPinLoginEnabled()
        fingerprint_sw.isChecked = localStorage.isFingerprintLoginEnabled()

        push_sw.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) localStorage.enablePush()
            else localStorage.disablePush()
        }

        pin_sw.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) localStorage.enablePin()
            else {
                localStorage.disablePin()
                fingerprint_sw.isChecked = false
            }
        }

        fingerprint_sw.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) localStorage.enableFingerprint()
            else localStorage.disableFingerprint()
        }

    }

    private fun showSignOutAlertDialog() {
        AlertDialog.Builder(requireContext()).setMessage(getString(R.string.confirm_signout))
            .setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes) { _, _ -> // todo show progress bar (fullscreen)
                Methods.deleteToken(viewLifecycleOwner) {
                    if (it.isEmpty()) {
                        PinUtil.clear(requireContext())
                        findNavController().navigate(
                            R.id.action_global_loginFragment,
                             bundleOf(LoginFragment.SET_LOGIN to true)
                        )
                    } else tt(it)
                }
            }.show()
    }
}
