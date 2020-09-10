package com.autonomad.ui.check_auto.history

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.navigateBack
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_check_auto_history.*
import org.koin.android.viewmodel.ext.android.viewModel

class CheckAutoHistoryFragment : Fragment(R.layout.fragment_check_auto_history) {
    private val viewModel: CheckAutoHistoryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
//        setOnClickListener()
        initialise()

    }

    fun initialise() {
        navigateBack(R.id.action_check_auto_history_to_home)
        viewModel.getData()
    }

    fun setObservers() {
        viewModel.history.observe(viewLifecycleOwner, Observer {
            for (i in 0 until it.size) {
                it[i].date = it[i].date.substringBefore("T")
                when {
                    i == 0 -> it[i].show = true
                    it[i].date == it[i - 1].date -> it[i].show = false
                    else -> it[i].show = true
                }
            }
            val adapter = HistoryAdapter(it, ::openTicketDetails)
            check_auto_history_rv.adapter = adapter
//            check_auto_history_rv.isVisible = isNotEmpty() todo madi
        })
        viewModel.toast.observe(viewLifecycleOwner, Observer {
            toast(it)
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_bar.visibility = View.VISIBLE
                main_content.visibility = View.GONE
            } else {
                progress_bar.visibility = View.GONE
                main_content.visibility = View.VISIBLE
            }
        })
    }

    private fun openTicketDetails(id: Int) {
        val bundle = bundleOf("id" to id)
        findNavController().navigate(R.id.action_check_auto_history_to_detailTicket, bundle)
    }
}