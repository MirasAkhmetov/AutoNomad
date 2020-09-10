package com.autonomad.ui.bottom_sheet.banking_cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.payment.BankingCardResult
import com.autonomad.databinding.BottomSheetItemBankingCardsBinding

class BankingCardAdapter : RecyclerView.Adapter<BankingCardAdapter.CarsViewHolder>() {

    var cars = emptyList<BankingCardResult>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = BottomSheetItemBankingCardsBinding.inflate(inflater, parent, false)
        return CarsViewHolder(dataBinding)
    }

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    class CarsViewHolder constructor(private val dataBinding: BottomSheetItemBankingCardsBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(car: BankingCardResult) {
            dataBinding.setVariable(BR.itemData, car)
            dataBinding.executePendingBindings()
        }
    }
}