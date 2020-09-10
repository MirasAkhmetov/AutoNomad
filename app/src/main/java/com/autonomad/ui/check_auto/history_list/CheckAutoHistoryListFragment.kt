package com.autonomad.ui.check_auto.history_list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.check_auto.CheckAutoHistory
import com.autonomad.data.models.check_auto.GarageCar
import com.autonomad.ui.bottom_sheet.check_auto.CheckAutoDeleteDialog
import com.autonomad.ui.bottom_sheet.insurance.DeleteDriverDialog
import com.autonomad.ui.check_auto.home.CheckAutoCarsAdapter
import com.autonomad.ui.check_auto.home.CheckAutoViewModel
import com.autonomad.ui.insurance.insurance_list.SerializableLiveData
import com.autonomad.utils.toast
import kotlinx.android.synthetic.main.fragment_check_auto_history_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable

class CheckAutoHistoryListFragment : Fragment(R.layout.fragment_check_auto_history_list) {
    private val checkAutoViewModel: CheckAutoViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
    }

    @Suppress("UNCHECKED_CAST")
    private fun observe() {
        checkAutoViewModel.carDeleted.observe(viewLifecycleOwner, Observer {
            toast("Автомобиль успешно удален")
            findNavController().navigate(R.id.action_check_auto_home_to_check_auto_home)
        })
        arguments?.let { args ->
            val isGaragePage = args.getBoolean("isGaragePage")
            if (isGaragePage) {
                val garageAutoList =
                    args.getSerializable("garageAutoList") as SerializableLiveData<List<GarageCar>>


                garageAutoList.observe(viewLifecycleOwner, Observer {
                    val adapter = CheckAutoCarsAdapter(
                        it,
                        requireContext(),
                        ::onCarClicked,
                        ::onDeleteClicked
                    )
                    list.adapter = adapter
                })


            } else {
                val ordersList =
                    args.getSerializable("ordersList") as SerializableLiveData<List<CheckAutoHistory>>

                ordersList.observe(viewLifecycleOwner, Observer {
                    val adapter = CheckAutoCarOrdersAdapter(
                        it,
                        requireContext(),
                        ::onCarClicked,
                        ::weDontKnow,
                        ::notFound
                    )
                    list.adapter = adapter
                })
            }
        }
    }

    fun notFound() {
        toast("Информация об авто не найдено")
    }

    fun onCarClicked(vin: String) {
        val args = bundleOf("vin" to vin)
        findNavController().navigate(R.id.action_check_auto_home_to_report, args)
    }

    fun onDeleteClicked(id: Int, view: View) {
        val bottomSheet = CheckAutoDeleteDialog(object : CheckAutoDeleteDialog.CheckAutoAction {
            override fun deleteClicked() {
                showDeleteDialog(id)
            }
        }, requireContext())
        bottomSheet.window
            ?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundResource(android.R.color.transparent);
        bottomSheet.show()
    }

    fun weDontKnow() {
        val bottomSheet = DeleteDriverDialog(object : DeleteDriverDialog.DeleteDriverAction {
            override fun deleteClicked() {
                //
            }
        }, requireContext())
        bottomSheet.window
            ?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundResource(android.R.color.transparent);
        bottomSheet.show()
    }

    private fun showDeleteDialog(carId: Int) {
        AlertDialog.Builder(requireContext()).setTitle("Подтвердите действие")
            .setMessage("Вы действительно хотите удалить данное авто?")
            .setNegativeButton(R.string.no, null).setPositiveButton(R.string.yes) { _, _ ->
//                checkAutoViewModel.deleteCar(carId)
                checkAutoViewModel.carDeleted.postValue(true)
            }
            .show()
    }


    companion object {
        fun newInstance(
            garageAutoList: SerializableLiveData<List<GarageCar>>? = null,
            ordersList: SerializableLiveData<List<CheckAutoHistory>>? = null,
            isGaragePage: Boolean = false
        ): Fragment {
            val args = Bundle()
            args.putBoolean("isGaragePage", isGaragePage)
            if (isGaragePage) {
                args.putSerializable("garageAutoList", garageAutoList as Serializable)
            } else {
                args.putSerializable("ordersList", ordersList as Serializable)
            }

            val fragment = CheckAutoHistoryListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}