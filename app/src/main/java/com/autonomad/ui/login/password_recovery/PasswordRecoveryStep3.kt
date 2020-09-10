package com.autonomad.ui.login.password_recovery

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.ui.login.AuthorizationFragment
import com.autonomad.utils.Methods
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_password_recovery_step3.*
import java.util.regex.Pattern

class PasswordRecoveryStep3 : AuthorizationFragment(R.layout.fragment_password_recovery_step3) {
    private val viewModel by viewModels<PasswordRecoveryStep3ViewModel>()
    private val eightSymbols = Pattern.compile("^" + ".{8,}" + "$")

    private val token by lazy { arguments?.getString("token") ?: Methods.key }

    private var passwordLatin = false
        set(value) {
            field = value
            ic_checked_1.setImageResource(checkDrawable(value))
        }
    private var passwordSymbolsCount = false
        set(value) {
            field = value
            ic_checked_2.setImageResource(checkDrawable(value))
        }
    private var passwordCapital = false
        set(value) {
            field = value
            ic_checked_3.setImageResource(checkDrawable(value))
        }
    private var passwordDigit = false
        set(value) {
            field = value
            ic_checked_4.setImageResource(checkDrawable(value))
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialise()
        setObservers()
        setOnClickListener()
    }

    private fun initialise() {
        password1.doAfterTextChanged {
            passwordLatin = it.toString().matches(Regex(".*[A-Za-z0-9]*"))
            passwordSymbolsCount = eightSymbols.matcher(it.toString()).matches()
            passwordCapital = it.toString().matches(Regex(".*[A-Z].*"))
            passwordDigit = it.toString().matches(Regex(".*\\d.*"))
            handleCheckEveryOne()
        }
        password2.doAfterTextChanged { handleCheckEveryOne() }
    }

    private fun setObservers() {
        viewModel.changed.observe(viewLifecycleOwner) {
            btn_next.text = if (it.isLoading) "" else getString(R.string.complete)
            pb_name_password.isVisible = it.isLoading
            it.onSuccess { onAuthorize(token) }
            it.onFail(::tt)
        }
    }

    private fun setOnClickListener() {
        iv_back.setOnClickListener { activity?.onBackPressed() }
        btn_next.setOnClickListener { viewModel.changePassword(password1.text.toString(), token) }
        visibility.setOnClickListener { checkVisibility() }
    }

    private fun handleCheckEveryOne() {
        btn_next.isEnabled = passwordLatin && passwordSymbolsCount && passwordCapital && passwordDigit &&
                password1.text.toString() == password2.text.toString()
    }

    private fun checkVisibility() {
        when (password1.inputType) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                password1.inputType = InputType.TYPE_CLASS_TEXT
                password2.inputType = InputType.TYPE_CLASS_TEXT
                password1.setSelection(password1.text.length)
                password2.setSelection(password2.text.length)
            }
            else -> {
                password1.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                password2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                password1.setSelection(password1.text.length)
                password2.setSelection(password2.text.length)
            }
        }
    }

    private fun checkDrawable(condition: Boolean) =
        if (condition) R.drawable.ic_default_checked else R.drawable.ic_default_not_checked
}
