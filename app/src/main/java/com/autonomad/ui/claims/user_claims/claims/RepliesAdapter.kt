package com.autonomad.ui.claims.user_claims.claims

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R

class RepliesAdapter(var categories: List<String>) :
    RecyclerView.Adapter<RepliesAdapter.RepliesAdapterViewHolder>() {

    class RepliesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val story_tag = view.story_tag
        fun bind(story: String) {
//            story_tag.text = story
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepliesAdapterViewHolder {
        return RepliesAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_claims_user_replies,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  categories.size
    }

    override fun onBindViewHolder(holder: RepliesAdapterViewHolder, position: Int) {
        holder.bind(categories[position])
    }
}