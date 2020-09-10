package com.autonomad.ui.login.login_page

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.login.AuthorizationFragment
import com.autonomad.utils.Methods
import com.autonomad.utils.onBackPress
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_login.*

class LoginPage : AuthorizationFragment(R.layout.fragment_login) {
    private val viewModel by viewModels<LoginPageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialise()
        setOnClickListener()
        setObservers()
    }

    private fun initialise() {
        onBackPress { activity?.finish() }

        password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        phone_number.doAfterTextChanged { text ->
            if (text == null || text.endsWith(")") || text.endsWith(" "))
                return@doAfterTextChanged
            else if (text.endsWith("(")) {
                phone_number.setText("")
            } else when (phone_number.text.length) {
                1 -> {
                    phone_number.setText(StringBuilder(text).insert(text.length - 1, "+7(").toString())
                    phone_number.setSelection(phone_number.text.length)
                }
                7 -> {
                    phone_number.setText(StringBuilder(text).insert(text.length - 1, ")").toString())
                    phone_number.setSelection(phone_number.text.length)
                }
                11, 14 -> {
                    phone_number.setText(StringBuilder(text).insert(text.length - 1, " ").toString())
                    phone_number.setSelection(phone_number.text.length)
                }
                17 -> {
                    phone_number.setText(StringBuilder(text).deleteCharAt(text.length - 1).toString())
                    phone_number.setSelection(phone_number.text.length)
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.token.observe(viewLifecycleOwner) {
            progress_bar.isVisible = it.isLoading
            login.isEnabled = !it.isLoading
            login.text = if (it.isLoading) "" else getString(R.string.login)
            it.onSuccess { onAuthorize(token) }
            if (it.isIdle) toast("Введены неверный номер или пароль. Проверьте данные и повторите попытку")
            it.onFail { message ->
                if (message.startsWith("412")) findNavController().navigate(
                    R.id.action_loginFragment_to_passwordRecoveryStep1,
                    bundleOf("phoneNumber" to message.replace("412+7", ""))
                )
                else tt(message)
            }
        }
    }

    private fun setOnClickListener() {
        button_forgot_password.setOnClickListener {
            Methods.hideKeyboard(view as View)
            findNavController().navigate(R.id.action_loginFragment_to_passwordRecoveryStep1)
        }

        login.setOnClickListener {
            Methods.hideKeyboard(view as View)
            viewModel.check(
                phone_number.text.toString().replace("(", "").replace(")", "").replace(" ", ""),
                password.text.toString()
            )
        }
        visibility.setOnClickListener {
            checkVisibility()
        }
    }

    private fun checkVisibility() {
        when (password.inputType) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                password.inputType = InputType.TYPE_CLASS_TEXT

            }
            else -> {
                password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }
}
