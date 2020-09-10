package com.autonomad.ui.bottom_sheet.insurance

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.databinding.BottomSheetAddPenalltyBinding
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.bottom_sheet.penalty.AddDriverViewModel
import com.autonomad.utils.Methods
import com.autonomad.utils.UinController
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.bottom_sheet_add_penallty.*

class AddInsurers : RoundBottomDialog() {
    private val viewModel by viewModels<AddDriverViewModel>()
    private var uinController: UinController? = null

    override fun getTheme(): Int = R.style.DialogStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetAddPenalltyBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            textHolder = "Данные застрахованного"
            lifecycleOwner = viewLifecycleOwner
        }
        setExpanded()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intervalTime = 3500.toLong() // 10 sec
        val handler = Handler()

        uinController = UinController(uin_layout) {
            if (it != null) viewModel.penaltySearch(it)
        }

        viewModel.driver.observe(viewLifecycleOwner) {
            add_driver.isEnabled = it.isSuccess
            it.onSuccess {
                vod_name.text = fullName
                Methods.hideKeyboard(view)
            }
            if (it.isIdle) {
                text_error.visibility = View.VISIBLE
                handler.postDelayed({ text_error.visibility = View.GONE }, intervalTime)
            }
        }

        add_driver.setOnClickListener {
            dismiss()
            val fullName = viewModel.driver.value?.item?.fullName
            val uin = uinController?.getUin() ?: viewModel.driver.value?.item?.uin
            if (fullName == null || uin == null) {
                toast("Введите ИИН")
                return@setOnClickListener
            }
            val args = (arguments ?: Bundle()).apply {
                putString(CheckPolis.UIN, uin)
                putString(CheckPolis.FULL_NAME, fullName)
            }
            CheckPolis.show(parentFragmentManager, args)
        }

        iv_back.setOnClickListener {
            CheckPolis.show(parentFragmentManager, arguments)
            dismiss()
        }
    }

    companion object {
        fun show(fragmentManager: FragmentManager, args: Bundle?) {
            AddInsurers().apply { arguments = args }.show(fragmentManager, "AddInsurers")
        }
    }
}