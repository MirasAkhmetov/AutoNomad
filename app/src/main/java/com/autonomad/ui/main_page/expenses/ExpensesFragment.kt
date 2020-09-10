package com.autonomad.ui.main_page.expenses

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.Expenses
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_main_page_expenses.*

class ExpensesFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_page_expenses, container, false)
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
        back_icon.setOnClickListener {
            activity?.onBackPressed()
        }
        expenses_rv.setLayoutManager(LinearLayoutManager(context))
        expenses_rv.setHasFixedSize(true)
        val expenses = ArrayList<Expenses>()
        expenses.add(Expenses("Автосервисы", "Техническое обслуживание", "05.12.19", 5000))
        expenses.add(Expenses("Штраф", "Онлайн оплата штрафа", "05.12.19", 12000))
        expenses.add(Expenses("Магазин", "Покупка запчасти", "05.12.19", 3000))
        val expenses_adapter =
            Expenses_Adapter(expenses)
        expenses_rv.adapter = expenses_adapter
        text_month.setOnClickListener {
            findNavController().navigate(R.id.action_expensesFragment_to_calendarFragment)
        }
        expenses_rv.addOnItemTouchListener(
            RecyclerItemClickListener(expenses_rv
                ,
                object :
                    RecyclerItemClickListener.OnItemClickListener {
                    @SuppressLint("ResourceType")
                    override fun onItemClick(view: View, position: Int) {
                        findNavController().navigate(R.id.action_expensesFragment_to_ticketsFragment)
                    }
                })
        )
    }
}