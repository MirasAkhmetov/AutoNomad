package com.autonomad.ui.main_page.expenses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.Expenses
import kotlinx.android.synthetic.main.item_expenses.view.*

class Expenses_Adapter(var expenses: ArrayList<Expenses>) :
    RecyclerView.Adapter<Expenses_Adapter.ExpensesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_expenses, parent, false)
        return ExpensesViewHolder(root)

    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        holder.bind(expenses.get(position))
    }

    class ExpensesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val services = view.services
        val date = view.date
        val name = view.name
        val cost = view.cost
        fun bind(expenses: Expenses) {
            services.text = expenses.service
            date.text = expenses.service
            name.text = expenses.name
            cost.text = expenses.cost.toString()
        }

    }

}