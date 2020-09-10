package com.autonomad.ui.profile.avto_profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.databinding.ItemProfileCarsBinding

class CarsProfileAdapter : RecyclerView.Adapter<CarsProfileAdapter.CarsProfileViewHolder>() {
    var cars: List<GarageCar> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemProfileCarsBinding.inflate(inflater, parent, false)
        return CarsProfileViewHolder(dataBinding)

    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: CarsProfileViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    class CarsProfileViewHolder constructor(
        private val dataBinding: ViewDataBinding
    ) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(cars: GarageCar) {
            dataBinding.setVariable(BR.itemData, cars)
            dataBinding.executePendingBindings()
        }
    }
}