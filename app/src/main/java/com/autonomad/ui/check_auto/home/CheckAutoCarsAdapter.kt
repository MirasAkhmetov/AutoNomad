package com.autonomad.ui.check_auto.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.databinding.ItemCheckAutoHomeBinding

class CheckAutoCarsAdapter(
    private val cars: List<GarageCar>,
    private val context: Context,
    private val onCarClicked: (vin: String) -> Unit,
    private val onDeleteCarClicked: (id: Int, view: View) -> Unit
) : RecyclerView.Adapter<HomeAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemCheckAutoHomeBinding.inflate(inflater, parent, false)
        return HomeAdapterViewHolder(dataBinding, onCarClicked, onDeleteCarClicked)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        holder.bind(cars[position])
    }
}

class HomeAdapterViewHolder(
    private val binding: ItemCheckAutoHomeBinding,
    private val onCarClicked: (vin: String) -> Unit,
    private val onDeleteCarClicked: (id: Int, view: View) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(car: GarageCar) {
        binding.setVariable(BR.itemData, car)
        binding.executePendingBindings()
        binding.report.setOnClickListener {
            onCarClicked.invoke(car.vin)
        }
        var expanded = false
        binding.container.setOnClickListener {
            expanded = !expanded
            binding.ivDropdown.animate().rotationBy(if (expanded) 180F else -180F)
                .setDuration(200).withEndAction {
                    if (expanded)
                        binding.ivDropdown.rotation = 180F
                    else
                        binding.ivDropdown.rotation = 0F
                }.start()
            if (expanded) {
                binding.layoutDetails.visibility = View.VISIBLE
            } else {
                binding.layoutDetails.visibility = View.GONE
            }
        }

        binding.ivDropdown.setOnClickListener {
            expanded = !expanded
            binding.ivDropdown.animate().rotationBy(if (expanded) 180F else -180F)
                .setDuration(200).withEndAction {
                    if (expanded)
                        binding.ivDropdown.rotation = 180F
                    else
                        binding.ivDropdown.rotation = 0F
                }.start()
            if (expanded) {
                binding.layoutDetails.visibility = View.VISIBLE
            } else {
                binding.layoutDetails.visibility = View.GONE
            }
        }
        binding.ivOptions.setOnClickListener {
            onDeleteCarClicked.invoke(car.id, binding.root)
        }
    }

//    override fun onArrowClick() {
//        expanded = !expanded
//        binding.ivDropdown.animate().rotationBy(if (expanded) 180F else -180F).setDuration(200)
//            .start()
//        binding.layoutDetails.isVisible = expanded
//    }
//
//    override fun onButtonClick() {
//        listener.onButtonClick(adapterPosition)
//    }
//
//    override fun onOptionsClick(view: View) {
//        listener.onOptionsClick(adapterPosition, view)
//    }
}