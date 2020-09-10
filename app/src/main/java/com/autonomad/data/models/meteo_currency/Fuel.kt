package com.autonomad.data.models.meteo_currency

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class City(val city: String, val marks: List<List<Mark>>)

@Parcelize
data class Mark(val markName: String, val markId: Int, val name: String, val price: String) : Parcelable
