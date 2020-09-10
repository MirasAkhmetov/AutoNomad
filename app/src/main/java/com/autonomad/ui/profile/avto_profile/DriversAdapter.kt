package com.autonomad.ui.profile.avto_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.main_page_car.GarageDriver
import com.autonomad.databinding.ItemDriverssDetailProfileBinding

class DriversAdapter : RecyclerView.Adapter<DriversAdapter.CategoriesAdapterViewHolder>() {

    var garageDriver: List<GarageDriver> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoriesAdapterViewHolder(ItemDriverssDetailProfileBinding.inflate(inflater, parent, false))
    }


    override fun getItemCount() = garageDriver.size

    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        holder.bind(garageDriver[position])
    }

    class CategoriesAdapterViewHolder(private val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(garageDriver: GarageDriver) {
            dataBinding.setVariable(BR.itemData, garageDriver)
            dataBinding.executePendingBindings()
        }
    }
}
