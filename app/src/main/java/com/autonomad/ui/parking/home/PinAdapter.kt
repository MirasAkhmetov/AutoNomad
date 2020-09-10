package com.autonomad.ui.parking.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.parking.ParkingZone
import com.autonomad.databinding.ItemParkingPinBinding

class PinAdapter(private val HomeViewModel: HomeViewModel) :
    RecyclerView.Adapter<PinAdapter.CarsViewHolder>() {
    var cars: List<ParkingZone> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemParkingPinBinding.inflate(inflater, parent, false)
        return CarsViewHolder(dataBinding, HomeViewModel, parent.context)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        holder.bind(cars[position])

    }

    class CarsViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val mainViewModel: HomeViewModel,
        val context: Context
    ) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(car: ParkingZone) {
            dataBinding.setVariable(BR.itemData, car)
            dataBinding.executePendingBindings()

        }
    }

    fun updateRepoList(cars: List<ParkingZone>) {
        this.cars = cars
        notifyDataSetChanged()
    }

}