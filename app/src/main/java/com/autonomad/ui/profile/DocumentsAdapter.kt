package com.autonomad.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import kotlinx.android.synthetic.main.bottom_sheet_item_cars.view.*

class DocumentsAdapter(var cards: List<String>) :
    RecyclerView.Adapter<DocumentsAdapter.DocumentsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_documents, parent, false)
        return DocumentsViewHolder(root)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    class DocumentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.name
        fun bind(bankingCard: String) {
            name.text = bankingCard
        }
    }


}