package com.autonomad.ui.claims.specialist_claims.feedback

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.ui.bottom_sheet.claim.ResponseGeneralProperties
import com.autonomad.ui.bottom_sheet.claim.ResponseOptions
import com.autonomad.utils.ScrollListener
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_claims_specialist_feedback_page.*

class FeedbackPage private constructor() : Fragment(R.layout.fragment_claims_specialist_feedback_page),
    FeedbackAdapter.OnClickListener, ScrollListener {

    private val position by lazy { arguments?.getInt(KEY_POSITION, -1) ?: -1 }
    private val feedbackAdapter by lazy { FeedbackAdapter(position, this) }
    private val viewModel by viewModels<FeedbackViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        initialize()
    }

    override fun onOptionsClick(view: View, responseId: Int) {
        ResponseGeneralProperties(
            requireContext(), when (position) {
                0 -> ResponseGeneralProperties.CREATED
                1 -> ResponseGeneralProperties.ACTIVE
                else -> ResponseGeneralProperties.COMPLETED
            }, object : ResponseOptions {
                override fun delete() {
                    viewModel.updateResponse(responseId, 3, position)
                }

                override fun finish() {
                    viewModel.updateResponse(responseId, 2, position)
                }

                override fun showReview() {
                    // todo(not implemented)
                }
            }
        ).apply {
            findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
            show()
        }
    }

    override fun onItemClick(responseId: Int) {
        val args = bundleOf("responseId" to responseId)
        findNavController().navigate(R.id.action_claim_specialist_feedback_to_detailClaim, args)
    }

    override fun loadMore() {
        viewModel.loadMore(position)
    }

    private fun initialize() {
        val (data, text) = when (position) {
            0 -> viewModel.requests to getString(R.string.no_responses)
            1 -> viewModel.active to getString(R.string.no_active_responses)
            2 -> viewModel.finished to getString(R.string.no_finished_responses)
            else -> throw IndexOutOfBoundsException()
        }
        data.observe(viewLifecycleOwner) {
            group_no_responses.isVisible = false
            it.onSuccess {
                feedbackAdapter.items = list
                if (next == null) {
                    feedbackAdapter.finish()
                    if (count == 0) {
                        group_no_responses.isVisible = true
                    }
                }
            }
            it.onFail(::tt)
        }
        tv_no_responses.text = text
    }

    private fun setAdapter() {
        feedbackAdapter.listener = this
        feedback_rv.adapter = feedbackAdapter
        feedback_rv.setHasFixedSize(true)
    }

    companion object {
        private const val KEY_POSITION = "position"

        fun newInstance(position: Int) = FeedbackPage().apply {
            arguments = bundleOf(KEY_POSITION to position)
        }
    }
}
