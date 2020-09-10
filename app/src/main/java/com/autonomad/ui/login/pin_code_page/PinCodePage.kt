package com.autonomad.ui.login.pin_code_page

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.pref.repo.LocalStorage
import com.autonomad.ui.MainActivity
import com.autonomad.utils.Methods
import com.autonomad.utils.PinUtil
import com.autonomad.utils.onBackPress
import kotlinx.android.synthetic.main.fragment_login_pin_code.*
import org.koin.android.ext.android.inject
import java.util.concurrent.Executor

// todo вход без пин кода
class PinCodePage : Fragment(R.layout.fragment_login_pin_code) {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var pinCode: String
    private lateinit var newPinCode: String

    private val localStorage: LocalStorage by inject()

    private val flag by lazy {
        if (!PinUtil.pinExists(requireContext())) FLAG_SET_PIN
        else arguments?.getInt(FLAG) ?: FLAG_ENTER_PIN
    }

    private val pins by lazy { listOf(line1, line2, line3, line4) }
    private val buttons by lazy {
        listOf(
            number_0,
            number_1,
            number_2,
            number_3,
            number_4,
            number_5,
            number_6,
            number_7,
            number_8,
            number_9,
            ic_touch_id,
            ic_delete
        )
    }

    private val activePin: Drawable?
        get() = ContextCompat.getDrawable(context as Context, R.drawable.ic_default_line_blue)

    private val disabledPin: Drawable?
        get() = ContextCompat.getDrawable(context as Context, R.drawable.ic_default_line_grey_2)

    @SuppressLint("SwitchIntDef")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        setOnClickListener()
        val biometricManager = BiometricManager.from(context as Context)
        ic_touch_id.isVisible = flag == FLAG_ENTER_PIN && localStorage.isFingerprintLoginEnabled()
        if (flag == FLAG_ENTER_PIN) {
            val name = Methods.name
            tv_greeting.text = if (name != null && name.trim().isNotEmpty()) getString(
                R.string.greeting,
                name
            ) else getString(R.string.greeting_nameless)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> biometricPrompt.authenticate(promptInfo)
                else -> ic_touch_id.visibility = View.INVISIBLE
            }
        }
    }

    private fun initialise() {
        text_pin_code.text = getString(
            when (flag) {
                FLAG_SET_PIN -> R.string.set_pin
                FLAG_UPDATE_PIN -> R.string.current_pin
                else -> R.string.enter_pin
            }
        )
        pinCode = ""
        newPinCode = ""
        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                navigateNext()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Сканер отпечатка пальца")
            .setNegativeButtonText("Отмена")
            .build()
    }

    private fun setOnClickListener() {
        onBackPress {
            when (flag) {
                FLAG_ENTER_PIN -> activity?.finish()
                FLAG_UPDATE_PIN -> findNavController().popBackStack()
                else -> {
                    AlertDialog.Builder(requireContext()).setMessage(getString(R.string.confirm_no_pin))
                        .setNegativeButton(R.string.no, null)
                        .setPositiveButton(R.string.yes) { _, _ ->
                            localStorage.disablePin()
                            navigateNext()
                        }.show()
                }
            }
        }

        buttons.forEachIndexed { index, view ->
            when {
                index < 10 -> view.setOnClickListener { check(index) }
                view.id == R.id.ic_touch_id -> view.setOnClickListener { biometricPrompt.authenticate(promptInfo) }
                view.id == R.id.ic_delete -> view.setOnClickListener { delete() }
            }
        }
        ic_exit.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun delete() {
        if (pinCode.isEmpty()) return
        pinCode = pinCode.dropLast(1)
        pins[pinCode.length].background = disabledPin
    }

    private var pinConfirmed = false
    private fun check(number: Int) {
        val text = number.toString()
        pins[pinCode.length].background = activePin
        pinCode += text
        if (pinCode.length < 4) return
        pins.forEach { it.background = disabledPin }
        if (flag == FLAG_SET_PIN || pinConfirmed) {
            if (newPinCode.isEmpty()) {
                newPinCode = pinCode
                text_pin_code.text = getString(R.string.repeat_pin)
            } else {
                if (newPinCode == pinCode) {
                    PinUtil.savePin(requireContext(), pinCode)
                    navigateNext()
                } else {
                    text_pin_code.text = getString(R.string.pins_does_not_match)
                }
            }
        } else if (PinUtil.isPinValid(requireContext(), pinCode)) {
            if (flag == FLAG_ENTER_PIN)
                navigateNext()
            else {
                text_pin_code.text = getString(R.string.new_pin)
                pinConfirmed = true
            }
        } else {
            text_pin_code.text = getString(R.string.invalid_pin)
        }
        pinCode = ""
    }

    private fun navigateNext() {
        if (flag == FLAG_UPDATE_PIN) findNavController().popBackStack()
        else {
            findNavController().navigate(
                R.id.action_pinCodePage_to_main_page,
                bundleOf(MainActivity.MESSAGE to MainActivity.PIN_ENTERED)
            )
        }
    }

    companion object {
        const val FLAG = "flag"
        const val FLAG_ENTER_PIN = 0
        const val FLAG_SET_PIN = 1
        const val FLAG_UPDATE_PIN = 2
    }
}
