package com.autonomad.ui.claims.user_claims.claims.claimoneone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.claim_user.ServiceCreate
import com.autonomad.data.models.claim_user.SubCategory
import com.autonomad.data.models.claims.ServiceModel
import com.autonomad.databinding.FragmentCreateTwoclaimBinding
import com.autonomad.ui.claims.user_claims.home.mycategory.CategoryListViewModel
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.Methods
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_create_twoclaim.*

class CreateClaim2 : BaseFragment() {
    private lateinit var ViewModel: FragmentCreateTwoclaimBinding
    private lateinit var sub_categ_adapter: CreateClaimTwoAdapter
    private var serviceCreateModel: ServiceCreate? = null
    private var claimServiceModel: ServiceModel? = null
    private var isEdit: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = FragmentCreateTwoclaimBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@CreateClaim2)
                .get(CategoryListViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return ViewModel.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceCreateModel = Gson().fromJson(arguments?.getString("serviceCreateModel"), ServiceCreate::class.java)
        claimServiceModel = Gson().fromJson(arguments?.getString("claimServiceModel"), ServiceModel::class.java)
        if (claimServiceModel != null) isEdit = true

        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
        btn_next_claim.setOnClickListener {
            var data = ""
            val mydatalist1: ArrayList<Int>? = ArrayList()
            val stList: List<SubCategory?>? = sub_categ_adapter.getStudentist()

            if (stList != null) {
                for (i in stList.indices) {
                    val singleStudent: SubCategory? = stList[i]
                    if (singleStudent != null) {
                        if (singleStudent.isfall) {
                            data = "$data${singleStudent.id}".trimIndent()
                            mydatalist1?.add(singleStudent.id)
                        }
                    }
                }
                if (mydatalist1?.isNullOrEmpty() == true) {
                    Toast.makeText(context, "Выберите категории", Toast.LENGTH_LONG).show()
                }else {
                    serviceCreateModel = ServiceCreate()
                    serviceCreateModel?.apply { subCategories = mydatalist1 }
                    val bundle = bundleOf("serviceCreateModel" to Gson().toJson(serviceCreateModel),
                    "claimServiceModel" to Gson().toJson(claimServiceModel))
                    findNavController().navigate(R.id.action_claimtwo_to_twomainclaim, bundle)
                }
            } else {
                Toast.makeText(context, "Выберите категории", Toast.LENGTH_LONG).show()
            }
        }

        ViewModel.viewmodel?.getCategoryId(
            Methods.getToken(),
            (serviceCreateModel?.categoryId ?: claimServiceModel?.subCategories!![0].category?.id ?: -1).toString()
        )
    }

    override fun initialise() {

    }

    override fun setObservers() {
        ViewModel.viewmodel?.subcategory?.observe(viewLifecycleOwner, Observer { items ->
            if (isEdit) {
                claimServiceModel?.subCategories?.map {  subCategoryModel ->
                    items.firstOrNull {
                        it.id == subCategoryModel.id
                    }?.isfall = true
                }
            }
            sub_categ_adapter.updateRepoList(items)
        })
    }

    override fun setAdapter() {
        val viewModel = ViewModel.viewmodel
        if (viewModel != null) {
            uslug_type_rv.layoutManager = LinearLayoutManager(context)

            uslug_type_rv.itemAnimator = DefaultItemAnimator()
            uslug_type_rv.setHasFixedSize(true)
            sub_categ_adapter = CreateClaimTwoAdapter(ViewModel.viewmodel!!, context as Context)
            uslug_type_rv.adapter = sub_categ_adapter
        }
    }

    override fun setOnClickListener() {

    }


}