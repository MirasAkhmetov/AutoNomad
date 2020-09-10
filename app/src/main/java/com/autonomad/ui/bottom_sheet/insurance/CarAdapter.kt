package com.autonomad.ui.bottom_sheet.insurance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.autonomad.BR
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.databinding.ItemInsuranceAddCarBinding
import com.autonomad.utils.PagedAdapter
import com.autonomad.utils.PagingViewHolder
import com.autonomad.utils.ScrollListener
import kotlinx.android.synthetic.main.item_insurance_add_car.view.*

class CarAdapter(override var scrollListener: ScrollListener?) :
    PagedAdapter<GarageCar, CarAdapter.CarViewHolder>(CarDiffUtil(), 5) {

    var checkedCar = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int) = CarViewHolder(
        ItemInsuranceAddCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        if (holder is CarViewHolder) {
            holder.bind(getItem(position), checkedCar == position)
            holder.itemView.container.setOnClickListener {
                if (checkedCar != position) checkedCar = position
            }
        }
    }

    class CarViewHolder(private val binding: ViewDataBinding) : PagingViewHolder(binding.root) {
        fun bind(car: GarageCar, isChecked: Boolean) {
            binding.setVariable(BR.car, car)
            binding.setVariable(BR.isChecked, isChecked)
            binding.executePendingBindings()
        }
    }

    private class CarDiffUtil : DiffUtil.ItemCallback<GarageCar>() {
        override fun areItemsTheSame(oldItem: GarageCar, newItem: GarageCar) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GarageCar, newItem: GarageCar) = oldItem == newItem
    }
}