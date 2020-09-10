package com.autonomad.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.SpecialOffer
import kotlinx.android.synthetic.main.item_special_offer.view.*

class SpecialOfferAdapter(var offer: ArrayList<SpecialOffer>) :
    RecyclerView.Adapter<SpecialOfferAdapter.SpecialOfferViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialOfferViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_special_offer, parent, false)
        return SpecialOfferAdapter.SpecialOfferViewHolder(root)

    }

    override fun getItemCount(): Int {
        return offer.size
    }

    override fun onBindViewHolder(holder: SpecialOfferViewHolder, position: Int) {
        holder.bind(offer.get(position))
    }

    class SpecialOfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val group = view.text_name_of_group
        val description = view.description
        fun bind(offer: SpecialOffer) {
            group.text = offer.group
            description.text = offer.description
        }

    }

}