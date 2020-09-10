package com.autonomad.ui.main_page.expenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.Ticket
import com.autonomad.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_receipts.*

class ReceiptsFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receipts, container, false)
    }

    override fun initialise() {

    }

    override fun setObservers() {

    }

    override fun setAdapter() {

    }

    override fun setOnClickListener() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tickets_rv.setLayoutManager(LinearLayoutManager(context))
        tickets_rv.setHasFixedSize(true)
        val tickets = ArrayList<Ticket>()
        tickets.add(Ticket("05.12.19", "5000₸", "В. Сладость П.", "Kaspi Bank*****2134"))
        tickets.add(Ticket("05.12.19", "5000₸", "В. Сладость П.", "Kaspi Bank*****2134"))
        tickets.add(Ticket("05.12.19", "5000₸", "В. Сладость П.", "Kaspi Bank*****2134"))
        tickets.add(Ticket("05.12.19", "5000₸", "В. Сладость П.", "Kaspi Bank*****2134"))
        tickets.add(Ticket("05.12.19", "5000₸", "В. Сладость П.", "Kaspi Bank*****2134"))
        tickets.add(Ticket("05.12.19", "5000₸", "В. Сладость П.", "Kaspi Bank*****2134"))
        val ticketsAdapter = ReceiptsAdapter(tickets)
        tickets_rv.adapter = ticketsAdapter
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }

    }
}