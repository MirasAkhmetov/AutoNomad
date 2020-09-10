package com.autonomad.ui.profile.settings.addresses

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.Address
import com.autonomad.data.models.AddressType
import com.autonomad.utils.timber
import com.autonomad.utils.toast
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile_settings_address_edit.*
import java.util.concurrent.TimeUnit

class EditAddressFragment : Fragment(R.layout.fragment_profile_settings_address_edit) {

    private val id by lazy { arguments?.getInt("id") }
    private val viewModel by viewModels<EditAddressViewModel> { EditAddressFactory(id) }
    private val types = AddressType.values().mapNotNull { it.title }
    private val typesAdapter by lazy { ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, types) }
    private val citiesAdapter by lazy { ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item) }

    private val progressLiveData = MediatorLiveData<Pair<Boolean, Boolean>>()

    private val flowableOnSubscribe by lazy {
        FlowableOnSubscribe<String> {
            et_city.doAfterTextChanged { et ->
                if (et?.toString().orEmpty().isNotEmpty()) iv_clear.isVisible = true
                it.onNext(et?.toString().orEmpty())
            }
        }
    }
    private val disposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        observe()
        setListeners()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun setAdapters() {
        et_title.setAdapter(typesAdapter)
        et_title.threshold = 0

        et_city.setAdapter(citiesAdapter)
        et_city.threshold = 0
    }

    private fun observe() {
        disposable.add(
            Flowable.create(flowableOnSubscribe, BackpressureStrategy.LATEST)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter { it.length > 1 }
                .map { it.toLowerCase().trim() }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel::loadCities)
        )

        progressLiveData.addSource(viewModel.address) {
            progressLiveData.value = (progressLiveData.value?.first ?: false) to it.isLoading
        }
        progressLiveData.addSource(viewModel.city) {
            progressLiveData.value = it.isLoading to (progressLiveData.value?.second ?: false)
        }
        progressLiveData.observe(viewLifecycleOwner) {
            group_content.isVisible = !it.first && !it.second
            et_title.isVisible = !it.first && !it.second
            progress_bar.isVisible = it.first || it.second
        }

        viewModel.address.observe(viewLifecycleOwner) {
            it.onSuccess { setAddress(this) }
            it.onFailure {
                toast("Не удалось закрыть адрес")
                timber(this)
            }
            group_content.isVisible = !it.isLoading
            et_title.isVisible = !it.isLoading
            progress_bar.isVisible = it.isLoading
        }

        viewModel.city.observe(viewLifecycleOwner) {
            it.onSuccess { et_city.setText(this) }
            group_content.isVisible = !it.isLoading
            et_title.isVisible = !it.isLoading
            progress_bar.isVisible = it.isLoading
        }

        viewModel.cities.observe(viewLifecycleOwner) {
            it.onSuccess {
                citiesAdapter.clear()
                citiesAdapter.addAll(list.map { c -> c.name })
                citiesAdapter.notifyDataSetChanged()
                if (et_city.isFocused) et_city.requestFocus()
            }
        }

        viewModel.result.observe(viewLifecycleOwner) {
            it.onSuccess {
                toast("Адрес успешно добавлен")
                activity?.onBackPressed()
            }
            it.onFailure {
                toast("Не удалось добавить адрес")
                timber(this)
            }
            progress_btn.isVisible = it.isLoading
            btn_save.isEnabled = !it.isLoading
            btn_save.text = if (it.isLoading) "" else getString(R.string.save)
        }
    }

    private fun setListeners() {
        iv_back.setOnClickListener { activity?.onBackPressed() }
        iv_clear.setOnClickListener {
            et_city.text.clear()
            iv_clear.isVisible = false
        }

        val checkFields: (Editable?) -> Unit = {
            btn_save.isEnabled =
                et_title.text.isNotEmpty() && et_city.text.isNotEmpty() && et_street.text.isNotEmpty() && et_house.text.isNotEmpty() && et_flat.text.isNotEmpty()
        }
        for (et in listOf(et_title, et_city, et_street, et_house, et_flat)) et.doAfterTextChanged(checkFields)

        btn_save.setOnClickListener {
            viewModel.saveAddress(
                et_title.text.toString(),
                et_city.text.toString(),
                et_street.text.toString(),
                et_house.text.toString(),
                et_flat.text.toString()
            )
        }
    }

    private fun setAddress(address: Address) {
        et_title.setText(address.description ?: AddressType.values().firstOrNull { it.string == address.type }?.title ?: "")
        et_city.setText(address.city.name)
        et_street.setText(address.extra.substringBeforeLast(", кв ").substringBeforeLast(" "))
        et_house.setText(address.extra.substringBeforeLast(", кв ").substringAfterLast(" ", ""))
        et_flat.setText(address.extra.substringAfterLast(", кв ", ""))
    }
}