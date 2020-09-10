package com.autonomad.data.models.insurance

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.autonomad.R
import com.autonomad.data.models.main_page_car.GarageDriver
import com.autonomad.data.models.main_page_car.Items
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

data class Insurance(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("iin")
    @Expose
    val iin: String,
    @SerializedName("tf_Number")
    @Expose
    val tf_Number: String,
    @SerializedName("global_id")
    @Expose
    val global_id: String,
    @SerializedName("policy_status")
    @Expose
    val policy_status: String,
    @SerializedName("insurance_company")
    @Expose
    val insurance_company: String,
    @SerializedName("start_date")
    @Expose
    val start_date: String,
    @SerializedName("end_date")
    @Expose
    val end_date: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("less_month")
    @Expose
    val less_month: Boolean,
    @SerializedName("dateCheck")
    @Expose
    var dateCheck: String,
    @SerializedName("full_Name")
    @Expose
    val full_Name: String,
    var show: Boolean
)

data class InsuranceHistory(
    val id: Int,
    val profile: Int,
    val driver: GarageDriver,
    val cars: InsuranceCar,
    val insuranceCheck: InsuranceCheck
) {
    fun getStatusBackgroundColor(context: Context): Drawable? {
        return when (insuranceCheck.status) {
            "Found" -> {
                if (isInsuranceValid()) {
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.background_light_blue_rectangle2
                    )
                } else {
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.background_light_orange_16dp_rectangle
                    )
                }
            }
            "Not Found" -> ContextCompat.getDrawable(
                context,
                R.drawable.background_dark_red_16dp_rectangle
            )
            else -> ContextCompat.getDrawable(context, R.drawable.background_grey_8_16dp_rectangle)
        }
    }

    fun getValidityColor(context: Context): Int {
        if (insuranceCheck.status.equals("Not Found"))
            return ContextCompat.getColor(
                context,
                R.color.Red
            )
        else if (isInsuranceValid())
            return ContextCompat.getColor(
                context,
                R.color.grey_3
            )
        else return ContextCompat.getColor(
            context,
            R.color.situational_red_error
        )
    }

    fun getValidityText(withDate: Boolean): String {
        if (insuranceCheck.status.equals("Not Found"))
            return "Данные отсутствуют"
        else if (isInsuranceValid() && withDate)
            return "Действительна до ${insuranceCheck.endDate}"
        else if (isInsuranceValid())
            return "Действительна"
        else return "Не действительна"
    }

    fun isInsuranceValid(): Boolean {
        try {
            val endDate = SimpleDateFormat("yyyy-MM-dd")
                .parse(insuranceCheck.endDate ?: throw NullPointerException())
            return endDate?.time ?: 0 > Calendar.getInstance().timeInMillis
        } catch (e: Exception) {
            return false
        }
    }
}

data class InsuranceCar(
    val id: Int,
    val profile: Int,
    @SerializedName("state_number") val stateNumber: String,
    @SerializedName("car_mark") val mark: Items,
    @SerializedName("car_model") val model: Items
)

data class InsuranceCheck(
    val id: Int,
    val logo: String,
    val iin: String,
    @SerializedName("tf_Number")
    val tfNumber: String,
    @SerializedName("global_id")
    val globalId: String,
    @SerializedName("policy_status")
    val policyStatus: String,
    @SerializedName("insurance_company")
    val company: String,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("end_date")
    val endDate: String?,
    val status: String?,
    @SerializedName("less_month")
    val lessMonth: Boolean,
    @SerializedName("dateCheck")
    val dateCheck: String,
    @SerializedName("full_Name")
    val fullName: String?,
    val isFavorite: Boolean
)