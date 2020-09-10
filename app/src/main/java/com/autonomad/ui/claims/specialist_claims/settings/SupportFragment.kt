package com.autonomad.ui.claims.specialist_claims.settings

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.Methods
import kotlinx.android.synthetic.main.fragment_claims_specialist_support.*

class SupportFragment : Fragment(R.layout.fragment_claims_specialist_support) {
    private var firstAnimating = false
    private var secondAnimating = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { activity?.onBackPressed() }

        tv_info_first.isVisible = Methods.supportFirstCardVisible
        iv_close_first.rotation = if (Methods.supportFirstCardVisible) 45f else 0f
        tv_info_second.isVisible = Methods.supportSecondCardVisible
        iv_close_second.rotation = if (Methods.supportSecondCardVisible) 45f else 0f

        iv_close_first.setOnClickListener { firstCardClick() }
        tv_question_first.setOnClickListener { firstCardClick() }
        iv_close_second.setOnClickListener { secondCardClick() }
        tv_question_second.setOnClickListener { secondCardClick() }

        cv_contact.setOnClickListener {
            findNavController().navigate(R.id.action_claim_specialist_support_to_contact)
        }
    }

    private fun secondCardClick() {
        if (!secondAnimating) {
            secondAnimating = true
            Methods.swapSecondCardVisibility()
            val isOpened = Methods.supportSecondCardVisible
            tv_info_second.isVisible = isOpened
            iv_close_second.animate().rotationBy(45f * if (isOpened) 1 else -1).setDuration(250).withEndAction {
                secondAnimating = false
            }.start()
        }
    }

    private fun firstCardClick() {
        if (!firstAnimating) {
            firstAnimating = true
            Methods.swapFirstCardVisibility()
            val isOpened = Methods.supportFirstCardVisible
            tv_info_first.isVisible = isOpened
            iv_close_first.animate().rotationBy(45f * if (isOpened) 1 else -1).setDuration(250).withEndAction {
                firstAnimating = false
            }.start()
        }
    }
}