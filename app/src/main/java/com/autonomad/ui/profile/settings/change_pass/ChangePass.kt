package com.autonomad.ui.profile.settings.change_pass

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.autonomad.R
import com.autonomad.data.models.login.Password
import com.autonomad.databinding.FragmentPasswordRecovChangeBinding
import com.autonomad.ui.profile.change_pass.ChangePassViewModel
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.Methods
import kotlinx.android.synthetic.main.fragment_password_recov_change.*
import kotlinx.android.synthetic.main.fragment_password_recovery_step3.ic_checked_1
import kotlinx.android.synthetic.main.fragment_password_recovery_step3.ic_checked_2
import kotlinx.android.synthetic.main.fragment_password_recovery_step3.ic_checked_3
import kotlinx.android.synthetic.main.fragment_password_recovery_step3.ic_checked_4
import kotlinx.android.synthetic.main.fragment_password_recovery_step3.password1
import kotlinx.android.synthetic.main.fragment_password_recovery_step3.password2
import kotlinx.android.synthetic.main.fragment_password_recovery_step3.visibility
import java.util.regex.Pattern

class ChangePass : BaseFragment() {
    private lateinit var ViewModel: FragmentPasswordRecovChangeBinding
    private val eightSymbols = Pattern.compile("^" + ".{8,}" + "$")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = FragmentPasswordRecovChangeBinding.inflate(inflater, container, false).apply {
            viewmodel =
                ViewModelProvider(this@ChangePass)
                    .get(ChangePassViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return ViewModel.root
    }

    override fun initialise() {
        whitebtn2()

        password1.doOnTextChanged { it, start, count, after ->
            if (it.toString().matches(Regex(".*[A-Za-z0-9]*"))) {
                ic_checked_1.setImageResource(R.drawable.ic_default_checked)
            } else {
                ic_checked_1.setImageResource(R.drawable.ic_default_not_checked)
            }

            if (eightSymbols.matcher(it.toString()).matches()) {
                ic_checked_2.setImageResource(R.drawable.ic_default_checked)
            } else {
                ic_checked_2.setImageResource(R.drawable.ic_default_not_checked)
            }

            if (it.toString().matches(Regex(".*[A-Z].*"))) {
                ic_checked_3.setImageResource(R.drawable.ic_default_checked)
            } else {
                ic_checked_3.setImageResource(R.drawable.ic_default_not_checked)
            }

            if (it.toString().matches(Regex(".*\\d.*"))) {
                ic_checked_4.setImageResource(R.drawable.ic_default_checked)
            } else {
                ic_checked_4.setImageResource(R.drawable.ic_default_not_checked)
            }
            handleCheckEveryOne()
        }
        password2.doOnTextChanged { text, start, count, after ->
            handleCheckEveryOne()
        }
    }

    override fun setObservers() {
        ViewModel.viewmodel?.success?.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    Methods.hideKeyboard(view as View)
                    ViewModel.viewmodel?.empty!!.value = null

                    Toast.makeText(context, "Пароль успешно изменен", Toast.LENGTH_SHORT).show()
                    Methods.hideKeyboard(view as View)
                    activity?.onBackPressed()
//                    val bundle = bundleOf("token" to arguments?.getString("token"))
//                    findNavController().navigate(R.id.action_global_pinCodePage, bundle)
                }
            }
        })
        ViewModel.viewmodel?.dataLoading?.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    whitebtn2()
                }
            }
        })
    }

    override fun setAdapter() {

    }

    override fun setOnClickListener() {
        btn1.setOnClickListener {
            Methods.hideKeyboard(view as View)
            activity?.onBackPressed()
        }
        btn2.setOnClickListener {
            ViewModel.viewmodel?.changePassword(
                requireView(),
                Password(password1.text.toString()),
                Methods.getToken()
            )

        }
        visibility.setOnClickListener {
            checkVisibility()
        }

        //navigateBack(R.id.action_global_loginFragment)
    }

    private fun handleCheckEveryOne() {
        if (!checkEveryOne(password1.text.toString())) {
            whitebtn2()
        } else {
            bluebtn2()
        }
    }

    private fun whitebtn2() {
        btn2.apply {
            background = ContextCompat.getDrawable(
                context as Context,
                R.drawable.background_grey_8_16dp_rectangle
            )
            setTextColor(ContextCompat.getColor(context, R.color.grey_6))
            isClickable = false
            isFocusable = false
            isEnabled = false
        }

    }

    private fun bluebtn2() {
        btn2.apply {
            background = ContextCompat.getDrawable(
                context as Context,
                R.drawable.background_blue_rectangle
            )
            setTextColor(ContextCompat.getColor(context, R.color.grey_9))
            isClickable = true
            isFocusable = true
            isEnabled = true
        }
    }

    private fun checkEveryOne(it: String): Boolean {
        return it.matches(Regex(".*[A-Za-z0-9]*")) && eightSymbols.matcher(it)
            .matches() && it.matches(
            Regex(".*[A-Z].*")
        ) && it.matches(Regex(".*\\d.*")) && password1.text.toString() == password2.text.toString()
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
                password1.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                password2.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                password1.setSelection(password1.text.length)
                password2.setSelection(password2.text.length)
            }
        }
    }
}