package com.autonomad.ui.insurance.page

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.databinding.FragmentInsurancePageBinding
import com.autonomad.databinding.FragmentInsurancePageBinding.*
import com.autonomad.utils.navigateBack
import com.autonomad.utils.timberE
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_insurance_page.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class InsurancePageFragment : Fragment() {

    private lateinit var binding: FragmentInsurancePageBinding
    private val viewModel by viewModels<InsurancePageViewModel> {
        InsurancePageViewModelFactory(arguments?.getInt("id") ?: 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = inflate(inflater, container, false).apply {
            isValid = false
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnBackPress()
        setObservers()

        group_content.isVisible = false
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

        sw_save.setOnClickListener {
            viewModel.save(sw_save.isChecked)
            sw_save.isEnabled = false
        }

        btn_buy.setOnClickListener {
            findNavController().navigate(R.id.action_insurance_page_to_insurance_policy)
        }
    }

    private fun setOnBackPress() {
        iv_back.setOnClickListener { activity?.onBackPressed() }
        navigateBack()
    }

    private fun setObservers() {
        viewModel.insurance.observe(viewLifecycleOwner) {
            it.onSuccess {
                group_content.isVisible = true
                progress_bar.isVisible = false
                binding.insurance = this
                sw_save.isChecked = insuranceCheck.isFavorite
                try {
                    val endDate =
                        SimpleDateFormat("yyyy-MM-dd").parse(insuranceCheck.endDate ?: throw NullPointerException())
                    binding.isValid = endDate?.time ?: 0 > Calendar.getInstance().timeInMillis
                } catch (e: ParseException) {
                   // Toast.makeText(context, "Неверный формат даты", Toast.LENGTH_SHORT).show()
                    timberE(e, "parseEndDate: ")
                } catch (e: NullPointerException) {
                    //Toast.makeText(context, "Не указана дата", Toast.LENGTH_SHORT).show()
                    timberE(e, "parseEndDate: ")
                }
            }
            it.onFailure {
                Toast.makeText(context, "Не удалось найти данные по страховке", Toast.LENGTH_SHORT).show()
                Log.d("InsuranceFragmentLogcat", "onViewCreated: $this")
            }
        }

        viewModel.save.observe(viewLifecycleOwner) {
//            sw_save.isEnabled = !it.isLoading
            it.onFailure {
                toast(this)
                sw_save.isEnabled = true
            }
            it.onSuccess { sw_save.isChecked = this }
        }
    }
}