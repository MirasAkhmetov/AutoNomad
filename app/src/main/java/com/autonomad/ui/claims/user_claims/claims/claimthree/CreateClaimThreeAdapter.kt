package com.autonomad.ui.claims.user_claims.claims.claimthree

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import kotlinx.android.synthetic.main.item_claims_create_three.view.*

class CreateClaimThreeAdapter(private val items: List<String>) :
    RecyclerView.Adapter<CreateClaimThreeAdapter.CreateClaimThreeViewHolder>() {

    var drivers: List<String> = emptyList()

    private var selectedPosition1: Int = -1
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CreateClaimThreeViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_claims_create_three, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CreateClaimThreeViewHolder, position: Int) {
        holder.bind(items[position])

        if (selectedPosition1 != -1 && selectedPosition == position){
            //selectedPosition=position

            holder.itemView.tv_category.setBackgroundResource(R.drawable.background_grey_3_12dp_rectangle)
            holder.itemView.tv_category.setTextColor(Color.WHITE)
        } else {
            //selectedPosition=position

//            selectedPosition1=-1
//            selectedPosition=-1
            holder.itemView.tv_category.setBackgroundResource(R.drawable.background_white_with_grey7_line)
            holder.itemView.tv_category.setTextColor(Color.parseColor("#1F2D3D"))
        }
    }

    class CreateClaimThreeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(title: String) {
            itemView.tv_category.text = title


        }


    }
    fun updateRepoList(drivers: List<String>) {
        this.drivers = drivers
        notifyDataSetChanged()
    }
}