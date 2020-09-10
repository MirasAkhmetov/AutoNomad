package com.autonomad.ui.bottom_sheet.main_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.databinding.BottomSheetMainPageAddCarBinding
import com.autonomad.ui.main_page.add_auto.SpecialViewModel
import com.autonomad.utils.RoundedBottomSheetDialogFragment
import com.autonomad.utils.addOnItemTouchListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_main_page_add_car.*
import kotlin.properties.Delegates


class AddCar(val data: String, val some_id: Int, val viewmodel: SpecialViewModel) : RoundedBottomSheetDialogFragment() {
    private val viewModel by viewModels<AddCarViewModel>()
    private lateinit var binding: BottomSheetMainPageAddCarBinding
    private lateinit var addCar: AddCarAdapter
    private var state by Delegates.notNull<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetMainPageAddCarBinding.inflate(LayoutInflater.from(context), container, false)
            .apply {
                viewmodel = viewModel
                lifecycleOwner = lifecycleOwner
            }
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as? BottomSheetDialog
            val bottomSheetInternal = d?.findViewById<View>(R.id.design_bottom_sheet) ?: return@setOnShowListener
            BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return binding.root
    }

    override fun setObservers() {
        viewModel.data.observe(viewLifecycleOwner, Observer { it ->
            println(it)
            addCar.updateRepoList(it)
        })
    }


    override fun initialise() {
        name.text = data
        when (data) {
            "Марка машины" -> {
                viewModel.getMarks("")
                state = 1
            }
            "Модель машины" -> {
                viewModel.getModels(some_id, "")
                state = 2
            }
            "Цвет машины" -> {
                viewModel.getColors()
                state = 3
                searchView.visibility = View.GONE
            }
            "Поколение машины" -> {
                viewModel.getGeneration(some_id)
                state = 4
                searchView.visibility = View.GONE
            }
            "Серия машины" -> {
                viewModel.getSeries(some_id)
                state = 5
                searchView.visibility = View.GONE
            }
            "Модификация машины" -> {
                viewModel.getModification(some_id, "")
                state = 6
            }


        }

        searchView.setOnQueryTextListener(object : OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                when (state) {
                    1 -> viewModel.getMarks(newText)
                    2 -> viewModel.getModels(some_id, newText)
                    6 -> viewModel.getModels(some_id, newText)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                when (state) {
                    1 -> viewModel.getMarks(query)
                    2 -> viewModel.getModels(some_id, query)
                    6 -> viewModel.getModels(some_id, query)
                }
                return false
            }
        })
    }

    override fun setAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            rv.layoutManager = LinearLayoutManager(context)
            rv.setHasFixedSize(true)
            addCar = AddCarAdapter(binding.viewmodel!!)
            rv.adapter = addCar

            rv.addOnItemTouchListener { _, position ->
                when (state) {
                    1 -> {
                        viewModel.data.value?.get(position)?.let { viewmodel.setMarks(it.name, it.id) }
                        dismiss()
                    }
                    2 -> {
                        viewModel.data.value?.get(position)?.let { viewmodel.setModel(it.name, it.id) }
                        dismiss()
                    }
                    3 -> {
                        viewModel.data.value?.get(position)?.let { viewmodel.setColor(it.name, it.id) }
                        dismiss()
                    }
                    4 -> {
                        viewModel.data.value?.get(position)?.let { viewmodel.setGeneration(it.name, it.id) }
                        dismiss()
                    }
                    5 -> {
                        viewModel.data.value?.get(position)?.let { viewmodel.setSeries(it.name, it.id) }
                        dismiss()
                    }
                    6 -> {
                        viewModel.data.value?.get(position)?.let { viewmodel.setModification(it.name, it.id) }
                        dismiss()
                    }
                }
            }

        }
    }

    override fun setOnClickListener() {
    }
}