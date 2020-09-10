package com.autonomad.ui.profile.driver_profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.BR
import com.autonomad.R
import com.autonomad.data.models.penalty.Driver
import com.autonomad.databinding.ItemProfileDriversBinding
import com.autonomad.utils.Methods
import kotlinx.android.synthetic.main.item_profile_drivers.view.*

class DriversPageAdapter(private val driverPageViewModel: DriverPageViewModel, val context: Context) :
    RecyclerView.Adapter<DriversPageAdapter.CarsProfileViewHolder>() {
    var driver: List<Driver> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemProfileDriversBinding.inflate(inflater, parent, false)
        return CarsProfileViewHolder(dataBinding, driverPageViewModel, context)

    }

    override fun getItemCount() = driver.size

    override fun onBindViewHolder(holder: CarsProfileViewHolder, position: Int) {
        holder.bind(driver[position])
    }

    class CarsProfileViewHolder constructor(
        private val dataBinding: ViewDataBinding,
        private val driverPageViewModel: DriverPageViewModel,
        val context: Context
    ) : RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(driver: Driver) {
            dataBinding.setVariable(BR.itemData, driver)
            dataBinding.executePendingBindings()

            val bottomSheet = GeneralDrivPage(object : GeneralDrivPage.Chosee {
                override fun delete() {
                    driverPageViewModel.deleteDriv(
                        Methods.getToken(),
                        driver.id.toString(),
                        driver
                    )
                }

                override fun cancel() {}
            }, context)
            bottomSheet.window?.findViewById<View>(R.id.design_bottom_sheet)
                ?.setBackgroundResource(android.R.color.transparent)

            dataBinding.root.logo_of_card.setOnClickListener {
                bottomSheet.show()
            }

            dataBinding.root.setOnClickListener {
                val bundle = bundleOf("id" to driver.id)
                it.findNavController().navigate(R.id.action_profile_to_driverPageDetail, bundle)
            }
        }
    }
}