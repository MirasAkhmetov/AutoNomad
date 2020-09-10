package com.autonomad.ui.bottom_sheet.main_page

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.main_page_car.Items
import com.autonomad.databinding.BottomSheetItemMainPageAddCarBinding

class AddCarAdapter(private val addCarViewModel: AddCarViewModel) :
    RecyclerView.Adapter<AddCarAdapter.CarsViewHolder>() {
    var items: List<Items> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = BottomSheetItemMainPageAddCarBinding.inflate(inflater, parent, false)
        return CarsViewHolder(dataBinding, addCarViewModel, parent.context)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        holder.bind(items[position])

    }

    class CarsViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val addCarViewModel: AddCarViewModel,
        val context: Context
    ) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(item: Items) {
            dataBinding.setVariable(BR.itemData, item)
            dataBinding.executePendingBindings()
        }
    }

    fun updateRepoList(items: List<Items>) {
        this.items = items
        notifyDataSetChanged()
    }

}