package com.autonomad.ui.penalty.penalties_of_person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentPenaltyDetailPenaltyBinding
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_penalty_detail_penalty.*

class PenaltyDetail : Fragment() {

    private val penaltyId by lazy { arguments?.getInt("id") ?: 0 }
    private val viewModel by viewModels<PenaltyDetailViewModel> { PenaltyDetailFactory(penaltyId) }
    private lateinit var binding: FragmentPenaltyDetailPenaltyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPenaltyDetailPenaltyBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener { activity?.onBackPressed() }
        pay.setOnClickListener { viewModel.makeOrder() }
        viewModel.url.observe(viewLifecycleOwner) {
            it.onSuccess {
                val bundle = bundleOf("url" to formUrl)
                findNavController().navigate(R.id.action_penaltyDetail_to_makeOrder, bundle)
            }
            it.onFailure { toast(this) }
        }
        viewModel.result.observe(viewLifecycleOwner) {
            it.onFailure { toast(this) }
        }
    }
}