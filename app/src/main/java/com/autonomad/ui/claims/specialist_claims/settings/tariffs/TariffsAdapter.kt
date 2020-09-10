package com.autonomad.ui.claims.specialist_claims.settings.tariffs

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claims.Tariff
import kotlinx.android.synthetic.main.item_claims_specialist_tarif.view.*

class TariffsAdapter : RecyclerView.Adapter<TariffsAdapter.TariffViewHolder>() {
    var items = emptyList<Tariff>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TariffViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_claims_specialist_tarif, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TariffViewHolder, position: Int) {
        val tariff = items[position]
        holder.itemView.apply {
            tv_count.text = tariff.count
            tv_price.text = context.getString(R.string.price, tariff.price.substringBeforeLast("."))
            tv_old_price.paintFlags = tv_old_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    class TariffViewHolder(view: View) : RecyclerView.ViewHolder(view)
}