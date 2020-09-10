package com.autonomad.ui.bottom_sheet.claim

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.autonomad.R
import com.autonomad.data.models.claims.MasterComplaintRequest
import com.autonomad.databinding.BottomSheetSmsClaimBinding
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.ui.bottom_sheet.penalty.MasterComplaintViewModel
import com.autonomad.utils.UinController
import kotlinx.android.synthetic.main.bottom_sheet_sms_claim.*

class SmskaBottomDialog(@IdRes private val action: Int, val mMasterId: Int) : RoundBottomDialog() {
    private val viewModel by viewModels<MasterComplaintViewModel>()
    private var mIsSpam: Boolean = false
    private var mIsContent: Boolean = false
    private var mIsFake: Boolean = false

    override fun getTheme(): Int = R.style.DialogStyle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BottomSheetSmsClaimBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setExpanded()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cbSpam.setOnCheckedChangeListener { buttonView, isChecked ->
            setBackground(buttonView, isChecked)
            mIsSpam = isChecked
        }
        cbContent.setOnCheckedChangeListener { buttonView, isChecked ->
            setBackground(buttonView, isChecked)
            mIsContent = isChecked
        }
        cbFake.setOnCheckedChangeListener { buttonView, isChecked ->
            setBackground(buttonView, isChecked)
            mIsFake = isChecked
        }

        iv_back.setOnClickListener { dismiss() }

        btnComplaint.setOnClickListener {
            if (etDescription.text.toString().isBlank()) {
                Toast.makeText(context, "Напишите описание проблемы", Toast.LENGTH_SHORT).show()
            } else {
                val model = MasterComplaintRequest()
                model.apply {
                    masterId = mMasterId
                    isSpam = mIsSpam
                    isInappropriateContent = mIsContent
                    isFakeInfo = mIsFake
                    description = etDescription.text.toString()
                }
                viewModel.complaintMaster(model)
            }
        }

        viewModel?.masterComplaint?.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Жалоба отправлена", Toast.LENGTH_SHORT).show()
            dismiss()
        })
    }

    private fun setBackground(view: View, isChecked: Boolean) {
        view.setBackgroundResource(if (isChecked) R.drawable.ic_default_line_blue else R.drawable.ic_default_line_grey_6)
        (view as CheckBox).setTextColor(ContextCompat.getColor(requireContext(), if (isChecked) R.color.white else R.color.PrimaryBlue))
    }
}