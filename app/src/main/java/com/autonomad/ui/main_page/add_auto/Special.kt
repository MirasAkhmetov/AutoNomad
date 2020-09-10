package com.autonomad.ui.main_page.add_auto

import com.autonomad.data.models.main_page_car.Car

interface Special {
    fun setData()

    fun setData(car: Car)

    fun setMarks(data: String, id: Int)

    fun setModel(data: String, id: Int)

    fun setColor(data: String, id: Int)

    fun setGeneration(data: String, id: Int)

    fun setSeries(data: String, id: Int)

    fun setModification(data: String, id: Int)

}