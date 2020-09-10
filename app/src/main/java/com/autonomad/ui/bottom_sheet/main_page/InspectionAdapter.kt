package com.autonomad.ui.bottom_sheet.main_page

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.data.models.main_page_car.Inspection
import com.autonomad.utils.formatDate
import com.autonomad.utils.timberE
import kotlinx.android.synthetic.main.item_main_page_inspection.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class InspectionAdapter : RecyclerView.Adapter<InspectionAdapter.InspectionViewHolder>() {
    var cars: List<GarageCar> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var items: List<Inspection> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InspectionViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_main_page_inspection, parent, false)
    )

    override fun getItemCount() = cars.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InspectionViewHolder, position: Int) {
        val car = cars[position]
        holder.itemView.apply {
            if (car.srts.isNotEmpty()) {
                setOnClickListener(null)
                group_with_srts.isVisible = true
                card_no_srts.isVisible = false
                tv_car_info.text = car.titleFirst
                items.firstOrNull { it.carId == car.id }?.let {
                    try {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(it.expirationDate)!!
                        val isValid = dateFormat > Date(System.currentTimeMillis())
                        val (image, color) = if (isValid) {
                            formatDate(
                                tv_inspection_expiration_date,
                                it.expirationDate,
                                "dd.MM.yyyy",
                                "yyyy-MM-dd",
                                "действителен до "
                            )
                            R.drawable.ic_good to R.color.grey_3
                        } else {
                            tv_inspection_expiration_date.text = "недействителен"
                            R.drawable.ic_bad to R.color.situational_red_error
                        }
                        iv_inspection_status.setImageResource(image)
                        tv_inspection_expiration_date.setTextColor(ContextCompat.getColor(context, color))
                    } catch (e: ParseException) {
                        timberE(e, "onBindViewHolder")
                    }
                }
            } else {
                group_with_srts.isVisible = false
                card_no_srts.isVisible = true
                tv_car_info_no_srts.text = car.titleFirst
            }
        }
    }

    class InspectionViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
