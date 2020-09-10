package com.autonomad.ui.main_page.expenses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.Ticket
import kotlinx.android.synthetic.main.item_receipt.view.*

class ReceiptsAdapter(var tickets: ArrayList<Ticket>) :
    RecyclerView.Adapter<ReceiptsAdapter.TicketsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        return TicketsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_receipt,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return tickets.size
    }

    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    class TicketsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val price = view.price
        val date = view.date
        val name_surname = view.name_surname
        val paid_by = view.paid_by
        fun bind(ticket: Ticket) {
            price.text = ticket.price
            date.text = ticket.date
            name_surname.text = ticket.name_surname
            paid_by.text = ticket.paid_by
        }
    }
}