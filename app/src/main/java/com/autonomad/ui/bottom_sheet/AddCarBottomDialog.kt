package com.autonomad.ui.bottom_sheet

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_sheet_add_car.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class AddCarBottomDialog(private val onSuccess: () -> Unit) : RoundBottomDialog() {

    private val viewModel by viewModels<AddCarViewModel>()

    private val searchSubscribe by lazy {
        FlowableOnSubscribe<String> { f ->
            et_state_number.doAfterTextChanged {
                f.onNext(if (it == null || it.length < 4) "" else it.toString())
            }
        }
    }
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setExpanded()
        return inflater.inflate(R.layout.bottom_sheet_add_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { dismiss() }
        btn_add_car.setOnClickListener {
            if (et_state_number.text.isEmpty()) {
                toast("Введите номер авто")
                return@setOnClickListener
            }
            val car = viewModel.car.value?.item
            if (car == null) viewModel.searchCar(et_state_number.text.toString())
            else viewModel.addCar(viewModel.car.value?.item?.regnum, et_vin.text.toString())
        }
        iv_close.setOnClickListener {
            et_state_number.text.clear()
            viewModel.clearCar()
        }
        et_state_number.filters = arrayOf(InputFilter.AllCaps())
        observe()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun observe() {
        viewModel.car.observe(viewLifecycleOwner) {
            group_enter_vin.isVisible = false
            it.onFail { message ->
                when {
                    message == AddCarViewModel.REGNUM_NOT_FOUND -> group_enter_vin.isVisible = true
                    message.toLowerCase(Locale.ROOT).contains("timeout") -> tt(
                        "Превышено время ожидания. Повторите попытку",
                        message
                    )
                    else -> tt(message)
                }
            }
            lbl_add_auto.text = if (it.item != null) it.item.markModel else getString(R.string.new_auto)
            btn_add_car.isEnabled = it.isSuccess
            progress_bar.isVisible = it.isLoading
            btn_add_car.text = if (it.isLoading) "" else getString(R.string.add)
        }
        viewModel.added.observe(viewLifecycleOwner) {
            it.onSuccess {
                toast("Авто успешно добавлено")
                onSuccess()
                dismiss()
            }
            it.onFail(::tt)
            progress_bar.isVisible = it.isLoading
            btn_add_car.isEnabled = !it.isLoading && viewModel.car.value?.isSuccess == true
            btn_add_car.text = if (it.isLoading) "" else getString(R.string.add)
        }

        disposable.add(
            Flowable.create(searchSubscribe, BackpressureStrategy.LATEST).debounce(1500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { if (format.matcher(it).matches()) viewModel.searchCar(it) }
        )
    }

    companion object {
        private val format =
            Pattern.compile("^([0-9]{3}[A-Z]{2,3}[0-9]{2})|([A-Z][0-9]{3}[A-Z]{3})|(HC[0-9]{4})|([A-Z][0-9]{6})|([0-9]{2}[A-Z]{2}[0-9]{2})|([A-Z]{2}[0-9]{4})|([A-Z]{2}[0-9]{2})|([0-9]{3}((MO)|(MP))[0-9]{2})|([0-9]{3}[A-Z]{2,3})")
    }
}