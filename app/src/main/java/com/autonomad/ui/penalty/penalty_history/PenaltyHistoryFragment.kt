package com.autonomad.ui.penalty.penalty_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentPenaltyHistoryBinding
import com.autonomad.utils.addOnItemTouchListener
import com.autonomad.utils.navigateBack
import com.autonomad.utils.timber
import kotlinx.android.synthetic.main.fragment_penalty_history.*

class PenaltyHistoryFragment : Fragment() {
    private val viewModel by viewModels<PenaltyHistoryViewModel>()
    private val penaltyHistoryAdapter = PenaltyHistoryAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentPenaltyHistoryBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        initialise()
        setupAdapter()
    }

    private fun setupAdapter() {
        penalty_history_rv.apply {
            setHasFixedSize(true)
            adapter = penaltyHistoryAdapter
            addOnItemTouchListener { _, position ->
                val bundle = bundleOf("id" to penaltyHistoryAdapter.items[position].id)
                findNavController().navigate(R.id.action_penalty_history_to_penaltyTicketFragment, bundle)
            }
        }
    }

    private fun observer() {
        viewModel.history.observe(viewLifecycleOwner) {
            it.onSuccess {
                penaltyHistoryAdapter.items = list.mapIndexed { index, detail ->
                    detail.show = index == 0 || list[index - 1].order.createdDate.substringBefore("T") !=
                            detail.order.createdDate.substringBefore("T")
                    detail
                }
            }
            it.onFailure {
                timber(this)
                Toast.makeText(context, "Не удалось загрузить историю платежей", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initialise() {
        navigateBack(R.id.action_penalty_history_to_penalty)
    }
}
