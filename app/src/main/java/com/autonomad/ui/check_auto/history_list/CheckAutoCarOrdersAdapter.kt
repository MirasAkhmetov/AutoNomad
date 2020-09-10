package com.autonomad.ui.check_auto.history_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.check_auto.CheckAutoHistory
import com.autonomad.databinding.ItemCheckAutoCarOrderedBinding

class CheckAutoCarOrdersAdapter(
    private val ordered: List<CheckAutoHistory>,
    private val context: Context,
    private val onCarClicked: (vin: String) -> Unit,
    private val weDontKnow: () -> Unit,
    private val notFound: () -> Unit
) : RecyclerView.Adapter<HomeAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemCheckAutoCarOrderedBinding.inflate(inflater, parent, false)
        return HomeAdapterViewHolder(dataBinding, onCarClicked, weDontKnow, notFound)
    }

    override fun getItemCount(): Int {
        return ordered.size
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        holder.bind(ordered[position])
    }
}

class HomeAdapterViewHolder(
    private val binding: ItemCheckAutoCarOrderedBinding,
    private val onCarClicked: (vin: String) -> Unit,
    private val weDontKnow: () -> Unit,
    private val notFound: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ordered: CheckAutoHistory) {
        binding.setVariable(BR.itemData, ordered)
        binding.executePendingBindings()
        binding.report.setOnClickListener {
            if (ordered.vehicle != null)
                onCarClicked.invoke(ordered.vehicle.vin_number)
            else {
                notFound.invoke()
            }
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
            weDontKnow.invoke()
        }
    }
//
//    override fun onButtonClick() {
//        listener.onButtonClick(adapterPosition)
//    }
//
//    override fun onOptionsClick(view: View) {
//        listener.onOptionsClick(adapterPosition, view)
//    }
}