package com.autonomad.ui.bottom_sheet.claim

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.main_page_car.Result
import com.autonomad.databinding.BottomSheetItemCarsclaimBinding
import com.autonomad.ui.bottom_sheet.cars.CarsViewModel

class CarsAdapter(private val mainPageViewModel: CarsViewModel) :
    RecyclerView.Adapter<CarsAdapter.CarsViewHolder>() {
    var cars: List<Result> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = BottomSheetItemCarsclaimBinding.inflate(inflater, parent, false)
        return CarsViewHolder(dataBinding, mainPageViewModel, parent.context)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        holder.bind(cars[position])

    }

    class CarsViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val mainViewModel: CarsViewModel,
        val context: Context
    ) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(car: Result) {
            dataBinding.setVariable(BR.itemData, car)
            dataBinding.executePendingBindings()

        }
    }

    fun updateRepoList(cars: List<Result>) {
        this.cars = cars
        notifyDataSetChanged()
    }

}