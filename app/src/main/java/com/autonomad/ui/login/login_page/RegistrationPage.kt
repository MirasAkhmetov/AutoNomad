package com.autonomad.ui.login.login_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.login.PhoneWithCode
import com.autonomad.ui.login.AuthorizationFragment
import com.autonomad.utils.Methods
import com.autonomad.utils.onBackPress
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_registration.*
import java.util.regex.Pattern

class RegistrationPage : AuthorizationFragment(R.layout.fragment_registration) {
    private val viewModel by viewModels<RegistrationViewModel>()
    private val eightSymbols = Pattern.compile("^" + ".{8,}" + "$")

    private var state = State.NamePassword
        set(value) {
            field = value
            group_register.isVisible = value == State.NamePassword
            group_phone_number.isVisible = value != State.NamePassword
            timerVisible = value == State.SmsCode
            et_phone_number.isEnabled = value != State.SmsCode
        }

    private var timerVisible = false
        set(value) {
            field = value
            et_sms_code.isVisible = value
            group_timer.isVisible = value
            if (value) countDownTimer.start() else countDownTimer.cancel()
        }

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

    private var countDownTimer = object : CountDownTimer(59000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            tv_timer.text = (String.format("%d:%02d", millisUntilFinished / (1000 * 60), millisUntilFinished / 1000))
        }

        override fun onFinish() {
            tv_timer.text = ""
            tv_resend_code.apply {
                setTextColor(ContextCompat.getColor(context, R.color.PrimaryBlue))
                text = getString(R.string.resend_code_now)
                isEnabled = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setObservers()
        setOnClickListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initialise() {
        state = State.NamePassword
        et_phone_number.doAfterTextChanged { text ->
            if (text == null || text.endsWith(")") || text.endsWith(" ")) return@doAfterTextChanged
            else if (text.endsWith("(")) et_phone_number.text.clear()
            else when (text.length) {
                1 -> {
                    et_phone_number.setText("+7($text")
                    et_phone_number.setSelection(et_phone_number.text.length)
                }
                7 -> {
                    et_phone_number.setText(StringBuilder(text).insert(text.length - 1, ")").toString())
                    et_phone_number.setSelection(et_phone_number.text.length)
                }
                11, 14 -> {
                    et_phone_number.setText(StringBuilder(text).insert(text.length - 1, " ").toString())
                    et_phone_number.setSelection(et_phone_number.text.length)
                }
            }
            checkSecondButton()
        }
        et_sms_code.doAfterTextChanged { checkSecondButton() }
        password1.doAfterTextChanged {
            passwordLatin = it.toString().matches(Regex(".*[A-Za-z0-9]*"))
            passwordSymbolsCount = eightSymbols.matcher(it.toString()).matches()
            passwordCapital = it.toString().matches(Regex(".*[A-Z].*"))
            passwordDigit = it.toString().matches(Regex(".*\\d.*"))
            handleCheckEveryOne()
        }
        password2.doAfterTextChanged { handleCheckEveryOne() }
        et_name.doAfterTextChanged { handleCheckEveryOne() }

        onBackPress {
            if (state != State.NamePassword) {
                state = State.NamePassword
                et_phone_number?.text?.clear()
                et_sms_code?.text?.clear()
            } else {
                Methods.name = null
                activity?.finish()
            }
        }
    }

    private fun setObservers() {
        viewModel.isSmsSent.observe(viewLifecycleOwner) {
            if (it) {
                et_phone_number.isEnabled = false
                state = State.SmsCode
            }
        }
        viewModel.token.observe(viewLifecycleOwner) {
            it.onFail(::tt)
            it.onSuccess {
                countDownTimer.cancel()
                onAuthorize(this)
            }
            pb_name_password.isVisible = it.isLoading && state == State.NamePassword
            pb_sms.isVisible = it.isLoading && state != State.NamePassword
            btn_next_1.text = if (it.isLoading) "" else getString(R.string.next)
            btn_next_2.text =
                if (it.isLoading) "" else getString(if (state == State.SmsCode) R.string.complete else R.string.receive_sms)
            checkSecondButton()
        }
    }

    private fun setOnClickListener() {
        visibility.setOnClickListener {
            when (password1.inputType) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                    password1.inputType = InputType.TYPE_CLASS_TEXT
                    password2.inputType = InputType.TYPE_CLASS_TEXT
                    password1.setSelection(password1.text.length)
                    password2.setSelection(password2.text.length)
                }
                else -> {
                    password1.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    password2.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    password1.setSelection(password1.text.length)
                    password2.setSelection(password2.text.length)
                }
            }
        }

        btn_next_1.setOnClickListener {
            et_phone_number.clearFocus()
            Methods.hideKeyboard(view as View)
            state = State.PhoneNumber
        }
        btn_next_2.setOnClickListener {
            if (state == State.SmsCode) {
                Methods.hideKeyboard(view as View)
                if (et_sms_code.text.isEmpty()) toast("Введите код из СМС сообщения")
                else viewModel.getToken(
                    PhoneWithCode(
                        et_phone_number.text.toString().replace("(", "").replace(")", "").replace(" ", ""),
                        et_sms_code.text.toString().toInt()
                    )
                )
            } else {
                Methods.hideKeyboard(view as View)
                viewModel.register(
                    et_name.text.toString(),
                    et_phone_number.text.toString().replace("(", "").replace(")", "").replace(" ", ""),
                    password1.text.toString()
                )
            }
        }
        tv_resend_code.setOnClickListener {
            viewModel.sendSMS()
            state = State.SmsCode
            tv_resend_code.apply {
                setTextColor(ContextCompat.getColor(context, R.color.grey_4))
                text = getString(R.string.resend_code)
                isEnabled = false
            }
        }
    }

    private fun handleCheckEveryOne() {
        btn_next_1.isEnabled = passwordLatin && passwordSymbolsCount && passwordCapital && passwordDigit &&
                password1.text.toString() == password2.text.toString() && et_name.text.isNotEmpty() &&
                viewModel.token.value?.isLoading != true
    }

    private fun checkSecondButton() {
        btn_next_2.isEnabled = et_phone_number.text.length == 16 && viewModel.token.value?.isLoading != true
    }

    private fun checkDrawable(condition: Boolean) =
        if (condition) R.drawable.ic_default_checked else R.drawable.ic_default_not_checked

    private enum class State {
        NamePassword, PhoneNumber, SmsCode
    }
}