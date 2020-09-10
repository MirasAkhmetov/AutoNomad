package com.autonomad.ui.claims.user_claims.claims.detail_claims

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.claims.ServiceModel
import com.autonomad.data.models.main_page_car.IsActivUpdate
import com.autonomad.databinding.FragmentDetailOfClaimsBinding
import com.autonomad.ui.bottom_sheet.claim.GeneralDetailClaim
import com.autonomad.ui.profile.avto_profile.CustomProgressDialog
import com.autonomad.utils.Methods
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_detail_of_claims.*


class DetailClaim : Fragment() {
    private lateinit var ViewModel: FragmentDetailOfClaimsBinding
    private lateinit var masters_adapter: DetailClaimAdapter
    private var claimServiceModel: ServiceModel? = null
    private val progressDialog = CustomProgressDialog()
    var intka: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = FragmentDetailOfClaimsBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@DetailClaim)
                .get(DetailClaimViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return ViewModel.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
        ViewModel.viewmodel?.getServId(
            Methods.getToken(),
            arguments?.getString("UIN").toString()
        )

        ViewModel.viewmodel?.getMasters(Methods.getToken(), arguments?.getString("UIN").toString())

        ViewModel.viewmodel?.masters?.observe(viewLifecycleOwner, Observer {
            masters_adapter.masters = it
        })


        setupAdapter()
        observer()

        ViewModel.viewmodel?.favtwo?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                progressDialog.dialog.dismiss()
                Toast.makeText(context, "Заявка удален", Toast.LENGTH_SHORT).show()

                activity?.onBackPressed()
            } else {
                progressDialog.dialog.dismiss()
                Toast.makeText(context, "Заявка не удален", Toast.LENGTH_SHORT).show()
            }

        })

        val bottomSheet = GeneralDetailClaim(object : GeneralDetailClaim.Chosee {

            override fun delete() {
                AlertDialog.Builder(requireContext()).setTitle("Подтвердите действие")
                    .setMessage("Вы уверены, что хотите удалить эту завку?")
                    .setNegativeButton("Нет", null)
                    .setPositiveButton("Да") { _, _ ->
                        ViewModel.viewmodel?.deleteSer(
                            Methods.getToken(),
                            arguments?.getString("UIN").toString()
                        )
                        progressDialog.show(context as Context, "Удаление заявка...")
                    }.show()
            }

            override fun edit() {
                val bundle = bundleOf("claimServiceModel" to Gson().toJson(claimServiceModel))
                findNavController().navigate(R.id.action_detailClaim2_to_createClaim2, bundle)
            }

            override fun share() {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Change text here to normal one")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)

            }

        }, context)
        bottomSheet.window?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundResource(android.R.color.transparent)

        ic_setting.setOnClickListener {
            bottomSheet.show()
        }

        ic_detail_info.setOnClickListener {
            val bundle = bundleOf("requestId" to claimServiceModel?.id)
            findNavController().navigate(R.id.detail_claimtwo_detailClaim, bundle)
        }

        activacia.setOnClickListener {
            if (intka == 1) {
                ViewModel.viewmodel?.patchIsActiv(
                    Methods.getToken(),
                    arguments?.getString("UIN").toString(),
                    IsActivUpdate(3)
                )
            } else {
                ViewModel.viewmodel?.patchIsActiv(
                    Methods.getToken(),
                    arguments?.getString("UIN").toString(),
                    IsActivUpdate(1)
                )
            }
        }

        ViewModel.viewmodel?.favBoldima?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                if (intka == 1) {
                    intka = 0
                    activacia.text = "Активировать"
                    activacia.setTextColor(ContextCompat.getColor(context as Context, R.color.situational_error))

                    txttt.text = "Неавктивна"
                    txttt.setTextColor(ContextCompat.getColor(context as Context, R.color.situational_red_error))

                } else {
                    intka = 1
                    activacia.text = "Остановить заявку"
                    activacia.setTextColor(ContextCompat.getColor(context as Context, R.color.PrimaryBlue))

                    txttt.text = "Активна"
                    txttt.setTextColor(ContextCompat.getColor(context as Context, R.color.situational_error))
                }
            } else {

                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun observer() {
        ViewModel.viewmodel?.servicess?.observe(viewLifecycleOwner, Observer {
            claimServiceModel = it
            intka = it.status?.value ?: -1
        })
    }

    private fun setupAdapter() {
        val viewModel = ViewModel.viewmodel
        if (viewModel != null) {

            val horizontalLayoutManager1 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            masters_rv.layoutManager = horizontalLayoutManager1
            masters_adapter = DetailClaimAdapter()
            masters_rv.adapter = masters_adapter
        }
    }

}