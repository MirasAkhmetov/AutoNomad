package com.autonomad.ui.penalty.penalties_of_person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentPenaltiesOfPersonBinding
import com.autonomad.utils.addOnItemTouchListener
import com.autonomad.utils.navigateBack
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_penalties_of_person.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PenaltiesOfPerson : Fragment() {
    private lateinit var binding: FragmentPenaltiesOfPersonBinding
    private val penaltyAdapter = PersonPenaltyAdapter()
    private val uin by lazy { arguments?.getString("UIN").orEmpty() }
    private val viewModel: PenaltiesOfPersonViewModel by viewModel { parametersOf(uin) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPenaltiesOfPersonBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
        navigateBack()
        sw_save.setOnClickListener {
            val checked = (it as? SwitchCompat)?.isChecked ?: return@setOnClickListener
            viewModel.saveDriver(checked)
            sw_save.isEnabled = false
        }
        setAdapter()
        observe()
    }

    private fun observe() {
        viewModel.result.observe(viewLifecycleOwner) {
            it.onSuccess { penaltyAdapter.penalties = result?.flatMap { date -> date.penalties } ?: emptyList() }
            it.onFail(::tt)
        }

        viewModel.driverSaved.observe(viewLifecycleOwner) {
            sw_save.isEnabled = !it.isLoading
            it.onSuccess { sw_save.isChecked = this }
            it.onFailure { toast(this) }
        }
    }

    private fun setAdapter() {
        penaltiesOfPerson_rv.adapter = penaltyAdapter
        penaltiesOfPerson_rv.addOnItemTouchListener { _, position ->
            val bundle = bundleOf("id" to (penaltyAdapter.penalties[position].id))
            findNavController().navigate(R.id.action_penaltiesOfPerson_to_penaltyDetail, bundle)
        }
    }
}