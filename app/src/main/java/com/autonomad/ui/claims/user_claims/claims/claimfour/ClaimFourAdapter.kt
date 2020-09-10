package com.autonomad.ui.claims.user_claims.claims.claimfour

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.data.models.main_page_car.Result
import com.autonomad.databinding.ItemClaimsFourBinding
import kotlinx.android.synthetic.main.item_claims_four.view.*

class ClaimFourAdapter(private val claimFourViewModel: ClaimFourViewModel, val context: Context) :
    RecyclerView.Adapter<ClaimFourAdapter.CreateClaimFourViewHolder>() {

    var cars: List<Result> = emptyList()

    var drivers: List<String> = emptyList()
    private var selectedPosition1: Int = -1
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CreateClaimFourViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemClaimsFourBinding.inflate(inflater, parent, false)
        return CreateClaimFourViewHolder(dataBinding, claimFourViewModel, context)
    }

    override fun getItemCount(): Int {
        return  cars.size
    }

    override fun onBindViewHolder(holder: CreateClaimFourViewHolder, position: Int) {
        holder.bind(cars[position])

        if (selectedPosition1 != -1 && selectedPosition == position){
            holder.itemView.img_check.visibility = View.VISIBLE
        } else {

            holder.itemView.img_check.visibility = View.GONE
        }
    }

    class CreateClaimFourViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val claimFourViewModel: ClaimFourViewModel,
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