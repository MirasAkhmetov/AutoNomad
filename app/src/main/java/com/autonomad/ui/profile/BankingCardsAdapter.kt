package com.autonomad.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.payment.BankingCardResult
import kotlinx.android.synthetic.main.item_profile_banking_cards.view.*

class BankingCardsAdapter(var cards: List<BankingCardResult>, val context: Context) :
    RecyclerView.Adapter<BankingCardsAdapter.BankingCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BankingCardViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_banking_cards, parent, false), context
    )

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: BankingCardViewHolder, position: Int) {
        holder.itemView.name.text =
            cards[position].card_first_six + "*****" + cards[position].card_last_four

        if (cards[position].card_type == "Visa") {
            holder.itemView.logo_of_card.setImageResource(R.drawable.ic_card_visa)
        } else {
            holder.itemView.logo_of_card.setImageResource(R.drawable.ic_banking_card_logo)
        }
    }

    class BankingCardViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view)
}