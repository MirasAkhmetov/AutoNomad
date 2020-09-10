package com.autonomad.ui.penalty.penalty_receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.autonomad.databinding.FragmentPenaltyCheckBinding
import com.autonomad.utils.timber
import kotlinx.android.synthetic.main.fragment_penalty_check.*

class PenaltyCheckFragment : Fragment() {
    private val viewModel by viewModels<PenaltyCheckViewModel>()
    private lateinit var binding: FragmentPenaltyCheckBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPenaltyCheckBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.penaltyHistorySearch(arguments?.getInt("id") ?: 0)
        ic_back.setOnClickListener { activity?.onBackPressed() }
        viewModel.result.observe(viewLifecycleOwner, Observer {
            it.onFailure {
                timber(this)
                Toast.makeText(context, "Не удалось загрузить квитанцию", Toast.LENGTH_SHORT).show()
            }
        })
    }
}