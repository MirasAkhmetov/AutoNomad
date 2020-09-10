package com.autonomad.ui.bottom_sheet.main_page

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
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import com.autonomad.utils.tt
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_sheet_main_page_add_inspection.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class AddInspectionBottomDialog(private val onComplete: (String, String) -> Unit) : RoundBottomDialog() {

    private val viewModel by viewModels<AddInspectionViewModel>()
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
        return inflater.inflate(R.layout.bottom_sheet_main_page_add_inspection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe()
        btn_add_inspection.setOnClickListener {
            onComplete(viewModel.car.value?.item?.regnum ?: "", et_srts.text.toString())
            dismiss()
        }
        iv_back.setOnClickListener { dismiss() }
        iv_close.setOnClickListener { et_state_number.text.clear() }
        et_state_number.filters = arrayOf(InputFilter.AllCaps())
        et_srts.filters = arrayOf(InputFilter.AllCaps())
        et_srts.doAfterTextChanged {
            btn_add_inspection.isEnabled = it != null && it.isNotEmpty() && viewModel.car.value?.isSuccess == true
        }

        disposable.add(Flowable.create(searchSubscribe, BackpressureStrategy.LATEST).debounce(1500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { if (format.matcher(it).matches()) viewModel.searchCar(it) }
        )
    }

    private fun observe() {
        viewModel.car.observe(viewLifecycleOwner) {
            lbl_state_number_not_found.isVisible = false
            group_srts.isVisible = it.isSuccess
            it.onFail { message ->
                when {
                    message == AddInspectionViewModel.REGNUM_NOT_FOUND -> lbl_state_number_not_found.isVisible = true
                    message.toLowerCase(Locale.ROOT).contains("timeout") -> tt(
                        "Превышено время ожидания. Повторите попытку",
                        message
                    )
                    else -> tt(message)
                }
            }
            progress_bar.isVisible = it.isLoading
            btn_add_inspection.isEnabled = et_srts.text.isNotEmpty() && it.isSuccess
            btn_add_inspection.text = if (it.isLoading) "" else getString(R.string.check)
            lbl_add_auto.text = if (it.item != null) it.item.markModel else getString(R.string.new_auto)
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    companion object {
        private val format =
            Pattern.compile("^([0-9]{3}[A-Z]{2,3}[0-9]{2})|([A-Z][0-9]{3}[A-Z]{3})|(HC[0-9]{4})|([A-Z][0-9]{6})|([0-9]{2}[A-Z]{2}[0-9]{2})|([A-Z]{2}[0-9]{4})|([A-Z]{2}[0-9]{2})|([0-9]{3}((MO)|(MP))[0-9]{2})|([0-9]{3}[A-Z]{2,3})")
    }
}