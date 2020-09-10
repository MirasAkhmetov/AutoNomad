package com.autonomad.ui.bottom_sheet.main_page

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.main_page_car.Items
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.ApiGarageFactory
import com.autonomad.utils.Methods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddCarViewModel : BaseViewModel() {

    lateinit var compositeDisposable: CompositeDisposable
    var data = MutableLiveData<List<Items>>()
    fun getMarks(name: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getNameOfMarks(Methods.getToken(), name, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                println(data.body()?.result)
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        this.data.value = data.body()?.result
                        if (this.data.value!!.isEmpty()) {
                            empty.value = true
                        }
                    }
                    404 -> {
                        dataLoading.value = false
                        empty.value = false

                    }
                    500 -> {
                        dataLoading.value = false
                        empty.value = false

                    }
                    else -> {
                        dataLoading.value = false
                        empty.value = false

                    }
                }


            }, {
                dataLoading.value = false
                empty.value = false

            })

        compositeDisposable.add(disposable)

    }

    fun getModels(mark_id: Int, name: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getNameOfModels(Methods.getToken(), mark_id, name, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {
                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        this.data.value = data.body()?.result
                        if (this.data.value!!.isEmpty()) {
                            empty.value = true
                        }
                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }


            }, {

            })

        compositeDisposable.add(disposable)

    }

    fun getColors() {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getColors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {

                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        this.data.value = data.body()?.result?.map { Items(it.id, it.name) }
                        if (this.data.value!!.isEmpty()) {
                            empty.value = true
                        }
                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }


            }, {

            })

        compositeDisposable.add(disposable)

    }

    fun getGeneration(car_model_id: Int) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getNameOfGenerations(Methods.getToken(), car_model_id, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {

                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        this.data.value = data.body()?.result
                        if (this.data.value!!.isEmpty()) {
                            empty.value = true
                        }
                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }


            }, {

            })

        compositeDisposable.add(disposable)

    }

    fun getSeries(car_generation_id: Int) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getSeries(Methods.getToken(), car_generation_id, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {

                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        this.data.value = data.body()?.result
                        println(this.data.value)
                        if (this.data.value!!.isEmpty()) {
                            empty.value = true
                        }
                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }


            }, {

            })

        compositeDisposable.add(disposable)

    }

    fun getModification(car_series_id: Int, text: String) {
        dataLoading.value = true
        compositeDisposable = CompositeDisposable()
        val disposable = ApiGarageFactory.instance.apiService
            .getNameOfModifications(Methods.getToken(), car_series_id, text, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                when (data.code()) {

                    200, 201 -> {
                        dataLoading.value = false
                        empty.value = false
                        this.data.value = data.body()?.result
                        println(this.data.value)
                        if (this.data.value!!.isEmpty()) {
                            empty.value = true
                        }
                    }
                    404 -> {

                    }
                    500 -> {

                    }
                    else -> {

                    }
                }


            }, {

            })

        compositeDisposable.add(disposable)

    }


}