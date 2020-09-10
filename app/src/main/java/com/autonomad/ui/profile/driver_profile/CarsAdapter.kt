package com.autonomad.ui.profile.driver_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.insurance.InsuranceCar
import com.autonomad.databinding.ItemCarsDetailProfileBinding
import com.autonomad.utils.OnItemIdSelected
import kotlinx.android.synthetic.main.item_cars_detail_profile.view.*

class CarsAdapter(private val onClick: OnItemIdSelected) : RecyclerView.Adapter<CarsAdapter.CategoriesAdapterViewHolder>() {

    var garageCars: List<InsuranceCar> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoriesAdapterViewHolder(ItemCarsDetailProfileBinding.inflate(inflater, parent, false), onClick)
    }

    override fun getItemCount() = garageCars.size

    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        holder.bind(garageCars[position])
    }

    class CategoriesAdapterViewHolder(private val dataBinding: ViewDataBinding, private val onClick: OnItemIdSelected) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(garageCars: InsuranceCar) {
            dataBinding.setVariable(BR.itemData, garageCars)
            dataBinding.executePendingBindings()
            dataBinding.root.iv_close.isVisible = true
            dataBinding.root.iv_close.setOnClickListener {
                onClick(garageCars.id)
                it.isVisible = false
            }
        }
    }
}