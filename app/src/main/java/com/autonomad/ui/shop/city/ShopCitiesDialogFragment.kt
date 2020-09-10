package com.autonomad.ui.services

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.login.City
import com.autonomad.ui.shop.city.ShopCitiesAdapter
import com.autonomad.utils.tt
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_fragment_cities.*
import java.util.concurrent.TimeUnit

class ShopCitiesDialogFragment : DialogFragment() {

    private val viewModel by activityViewModels<ShopCityViewModel>()
    private val flowableOnSubscribe by lazy {
        FlowableOnSubscribe<String> {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    it.onNext(query.orEmpty())
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    it.onNext(newText.orEmpty())
                    return true
                }
            })
        }
    }
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_cities, container, false)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout((resources.displayMetrics.widthPixels * 0.9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable.add(
            Flowable.create(flowableOnSubscribe, BackpressureStrategy.LATEST).debounce(1000, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::getCities)
        )

        viewModel.cities.observe(viewLifecycleOwner) {
            it.onSuccess {
                val adapter = ShopCitiesAdapter(list, ::pickCity)
                rv_cities.adapter = adapter
            }
            it.onFail(::tt)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.getCities()
        findNavController().navigate(R.id.shop_home)
        super.onDismiss(dialog)
    }

    private fun pickCity(city: City) {
        viewModel.pickCity(city.id)
        dismiss()
    }
}