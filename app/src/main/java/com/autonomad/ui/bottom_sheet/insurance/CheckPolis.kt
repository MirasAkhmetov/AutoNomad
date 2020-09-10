package com.autonomad.ui.bottom_sheet.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.insurance.InsuranceCheck
import com.autonomad.data.models.insurance.NewCheck
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.insurance.check.InsuranceCheckViewModel
import com.autonomad.utils.Status
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.bottom_sheet_check_polis.*

class CheckPolis : RoundBottomDialog() {

    private val viewModel by viewModels<InsuranceCheckViewModel>()

    private val observer by lazy {
        Observer<Status<InsuranceCheck>> {
            btn_check.isEnabled = !it.isLoading
            btn_check.text = if (it.isLoading) "" else getString(R.string.check)
            progress_bar.isVisible = it.isLoading
            it.onSuccess {
                val dest = arguments?.getInt("dest", -1)
                if (dest != null && dest != -1) {
                    findNavController().navigate(
                        dest,
                        bundleOf("id" to id, "toHistory" to true)
                    )
                    dismiss()
                } else {
                    findNavController().navigate(
                        R.id.action_main_page_to_insurance_home,
                        bundleOf("id" to id, "toHistory" to true)
                    )
                    dismiss()
                }
            }
            it.onFail(::tt)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_check_polis, container, false)
        setExpanded()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uin = arguments?.getString(UIN)
        val fullName = arguments?.getString(FULL_NAME)
        val title = arguments?.getString(TITLE)
        val stateNumber = arguments?.getString(STATE_NUMBER)

        if (uin != null && fullName != null) {
            lbl_add_insurer.isVisible = false
            lbl_add_insurer_info.isVisible = false
            tv_add_insurer.isVisible = true
            tv_add_insurer.text = fullName
        }

        if (stateNumber != null) {
            lbl_add_auto.isVisible = false
            lbl_add_auto_info.isVisible = false
            tv_add_car.isVisible = true
            tv_add_car.text = title
        }

        btn_check.isEnabled = if (uin != null && fullName != null && stateNumber != null) {
            btn_check.setOnClickListener {
                viewModel.setData(NewCheck(uin, stateNumber))
                viewModel.result.observe(viewLifecycleOwner, observer)
            }
            true
        } else false
        iv_back.setOnClickListener {
            dismiss()
        }
        add_insurer.setOnClickListener {
            dismiss()
            AddInsurers.show(parentFragmentManager, arguments)
        }
        add_auto.setOnClickListener {
            dismiss()
            CarsListFragment.show(parentFragmentManager, arguments)
        }
    }

    companion object {
        const val FULL_NAME = "fullName"
        const val UIN = "uin"
        const val STATE_NUMBER = "stateNumber"
        const val TITLE = "title"

        fun show(manager: FragmentManager, args: Bundle? = null) {
            val dialog = CheckPolis().apply { arguments = args }
            dialog.show(manager, "CheckPolisDialog")
        }
    }
}