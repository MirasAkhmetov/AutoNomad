package com.autonomad.ui.login.password_recovery

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentPasswordRecoveryStep1Binding
import com.autonomad.ui.login.AuthorizationFragment
import com.autonomad.utils.Methods
import com.autonomad.utils.onBackPress
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_password_recovery_step1.*

class PasswordRecoveryStep1 : AuthorizationFragment() {
    private val viewModel by viewModels<PasswordRecoveryStep1ViewModel>()
    private var isActivation = false
        set(value) {
            field = value
            lbl_enter_number.isVisible = !value
            tv_title.text = getString(if (value) R.string.account_verification else R.string.password_recovery)
        }

    private val countDownTimer = object : CountDownTimer(59000, 1000) {
        override fun onFinish() {
            tv_resend_code.apply {
                text = getString(R.string.resend_code_now)
                isEnabled = true
            }
            tv_timer.text = ""
        }

        override fun onTick(millisUntilFinished: Long) {
            tv_resend_code.isEnabled = false
            tv_timer.text = String.format("%d:%02d", millisUntilFinished / (1000 * 60), millisUntilFinished / 1000)
        }
    }

    private var isSmsSent = false
        set(value) {
            field = value
            et_sms_code.isVisible = value
            et_phone_number.isEnabled = !value
            group_timer.isVisible = value
            lbl_enter_number.isVisible = !value && !isActivation
            if (value) countDownTimer.start() else countDownTimer.cancel()
            checkButton()
        }

    private val buttonText: String
        get() = getString(if (!isSmsSent) R.string.receive_sms else R.string.next)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentPasswordRecoveryStep1Binding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialise()
        setOnClickListener()
        setObservers()
    }

    private fun initialise() {
        btn_next.text = buttonText
        et_phone_number.doAfterTextChanged { text ->
            if (text == null || text.endsWith(")") || text.endsWith(" ")) return@doAfterTextChanged
            else if (text.endsWith("(")) et_phone_number.text.clear()
            else when (et_phone_number.text.length) {
                1 -> {
                    et_phone_number.setText(StringBuilder(text).insert(text.length - 1, "+7(").toString())
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
            checkButton()
        }
        et_sms_code.doAfterTextChanged {
            if (it?.length in 3..5) checkButton()
        }
        isSmsSent = false

        arguments?.getString("phoneNumber")?.let {
            for (c in it) et_phone_number.append("$c")
            isActivation = true
            viewModel.sendSMS(getPhoneNumber())
        }
    }

    private fun setObservers() {
        viewModel.smsSent.observe(viewLifecycleOwner) {
            it.onSuccess { isSmsSent = this }
            it.onFail { message ->
                if (message == "Not Found") toast(getString(R.string.number_not_found))
                else tt(message)
            }
            checkButton()
        }
        viewModel.confirm.observe(viewLifecycleOwner) {
            if (!it.isLoading) et_sms_code.isEnabled = true
            it.onSuccess {
                countDownTimer.cancel()
                if (isActivation) onAuthorize(this)
                else findNavController().navigate(
                    R.id.action_passwordRecoveryStep1_to_passwordRecoveryStep3,
                    bundleOf("token" to this)
                )
            }
            it.onFail(::tt)
            checkButton()
        }
    }

    private fun setOnClickListener() {
        iv_back.setOnClickListener {
            Methods.hideKeyboard(view as View)
            activity?.onBackPressed()
        }
        btn_next.setOnClickListener {
            if (!isSmsSent) viewModel.sendSMS(getPhoneNumber())
            else {
                val code = et_sms_code.text.toString()
                try {
                    viewModel.enterCode(code.toInt(), isActivation)
                    et_sms_code.isEnabled = false
                } catch (e: NumberFormatException) {
                    toast("Неверный формат кода")
                }
            }
        }
        tv_resend_code.setOnClickListener {
            viewModel.sendSMS(getPhoneNumber())
            it.isEnabled = false
            (it as? TextView)?.text = getString(R.string.resend_code)
        }
        onBackPress {
            countDownTimer.cancel()
            if (isSmsSent && !isActivation) isSmsSent = false
            else findNavController().popBackStack()
        }
    }

    private fun getPhoneNumber() = et_phone_number.text.toString().replace("(", "").replace(")", "").replace(" ", "")

    private fun checkButton() {
        btn_next.isEnabled = et_phone_number.text.length == 16 &&
                (isSmsSent == (et_sms_code.text.length > 3)) &&
                viewModel.smsSent.value?.isLoading != true &&
                viewModel.confirm.value?.isLoading != true
        btn_next.text =
            if (viewModel.smsSent.value?.isLoading == true || viewModel.confirm.value?.isLoading == true) "" else buttonText
    }
}