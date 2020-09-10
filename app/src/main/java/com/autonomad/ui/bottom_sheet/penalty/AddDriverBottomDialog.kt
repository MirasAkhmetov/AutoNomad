package com.autonomad.ui.bottom_sheet.penalty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.BottomSheetAddPenalltyBinding
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.utils.Methods
import com.autonomad.utils.UinController
import kotlinx.android.synthetic.main.bottom_sheet_add_penallty.*

class AddDriverBottomDialog(@IdRes private val action: Int) : RoundBottomDialog() {
    private val viewModel by viewModels<AddDriverViewModel>()
    private var uinController: UinController? = null

    override fun getTheme(): Int = R.style.DialogStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetAddPenalltyBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            textHolder = "Новый водитель"
            lifecycleOwner = viewLifecycleOwner
        }
        setExpanded()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uinController = UinController(uin_layout) {
            if (it != null) viewModel.penaltySearch(it)
        }

        viewModel.driver.observe(viewLifecycleOwner) {
            add_driver.isEnabled = it.isSuccess
        }

        add_driver.setOnClickListener {
            val uin = uinController?.getUin() ?: viewModel.driver.value?.item?.uin
            if (uin != null) {
                val bundle = bundleOf("UIN" to uin)
                findNavController().navigate(action, bundle)
                Methods.hideKeyboard(view)
            }
            dismiss()
        }

        iv_back.setOnClickListener {
            dismiss()
        }
    }
}