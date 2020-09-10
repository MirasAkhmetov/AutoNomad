package com.autonomad.ui.services

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.utils.navigateBack
import kotlinx.android.synthetic.main.fragment_services.*

class ServicesFragment : Fragment(R.layout.fragment_services) {
    private val viewModel by activityViewModels<ServicesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ServicesAdapter(childFragmentManager, 1)

        pager.adapter = adapter

        tv_city.setOnClickListener { CitiesDialogFragment().show(childFragmentManager, "CitiesDialog") }

        navigateBack(R.id.action_global_main_page)
        viewModel.user.observe(viewLifecycleOwner) { tv_city.text = it.item?.city?.name ?: getString(R.string.select_city) }
    }
}