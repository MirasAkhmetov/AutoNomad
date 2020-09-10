package com.autonomad.ui.main_page.tech_inspection

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.databinding.FragmentMainPageTechInspectionBinding
import com.autonomad.utils.timber
import com.autonomad.utils.timberE
import com.autonomad.utils.toast
import com.autonomad.utils.tt
import kotlinx.android.synthetic.main.fragment_main_page_tech_inspection.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TechInspectionFragment : Fragment() {

    private val carId by lazy { arguments?.getInt(CAR_ID) ?: -1 }
    private val stateNumber by lazy { arguments?.getString(STATE_NUMBER) ?: "" }
    private val srts by lazy { arguments?.getString(SRTS) ?: "" }
    private val viewModel by viewModels<TechInspectionViewModel> { TechInspectionFactory(carId, stateNumber, srts) }
    private lateinit var binding: FragmentMainPageTechInspectionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainPageTechInspectionBinding.inflate(inflater, container, false).apply {
            viewModel = this@TechInspectionFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        binding.isValid = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iv_back.setOnClickListener { activity?.onBackPressed() }
        observe()
        setSpannable()
        sw_save.setOnClickListener {
            viewModel.saveInspection()
            sw_save.isEnabled = false
        }
    }

    private fun observe() {
        viewModel.check.observe(viewLifecycleOwner) {
            timber(it)
            it.onFail {
                toast("Переданы неверные данные")
                activity?.onBackPressed()
            }
        }
        viewModel.inspection.observe(viewLifecycleOwner) {
            timber(it.toString())
            it.onSuccess {
                try {
                    val endDate = SimpleDateFormat("yyyy-MM-dd").parse(expirationDate)!!
                    binding.isValid = endDate.time > Calendar.getInstance().timeInMillis
                } catch (e: ParseException) {
                    toast("Неверный формат даты")
                    timberE(e, "observeInspection: parseEndDate")
                } catch (e: NullPointerException) {
                    toast("Не указана дата")
                    timberE(e, "observeInspection: null date")
                }
            }
            it.onFailure {
                Toast.makeText(context, "Не удалось найти данные по ТО", Toast.LENGTH_SHORT).show()
                timber(this)
            }
        }

        viewModel.saveResult.observe(viewLifecycleOwner) {
            sw_save.isEnabled = !it.isLoading
            it.onSuccess { sw_save.isChecked = this }
            it.onFail { message ->
                sw_save.isChecked = !sw_save.isChecked
                tt(message)
            }
        }
    }

    private fun setSpannable() {
        val ref = tv_src.text.toString()
        val refSpan = SpannableString(ref)
        refSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.grey_2)),
            0,
            ref.indexOf(":") + 1,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        tv_src.text = refSpan

        val dataPeriod = tv_data_period.text.toString()
        val dataSpan = SpannableString(dataPeriod)
        dataSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.grey_3)),
            0,
            dataPeriod.indexOf(":") + 1,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        tv_data_period.text = dataSpan
    }

    companion object {
        const val CAR_ID = "carId"
        const val STATE_NUMBER = "stateNumber"
        const val SRTS = "srts"
    }
}