package com.autonomad.ui.claims.user_claims.claims.claimfour

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.claim_user.ServiceCreate
import com.autonomad.data.models.claims.ServiceModel
import com.autonomad.data.models.main_page_car.Result
import com.autonomad.databinding.FragmentCreateFourclaimBinding
import com.autonomad.ui.bottom_sheet.AddCarBottomDialog
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.Methods
import com.google.gson.Gson
import com.autonomad.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.fragment_create_fourclaim.*
import kotlinx.android.synthetic.main.fragment_create_fourclaim.btn_next_claim
import kotlinx.android.synthetic.main.fragment_create_fourclaim.ic_back
import kotlinx.android.synthetic.main.fragment_create_fourclaim.uslug_type_rv
import kotlinx.android.synthetic.main.item_claims_four.view.*

class CreateClaimFour : BaseFragment() {
    private lateinit var ViewModel: FragmentCreateFourclaimBinding
    private val viewModel by viewModels<ClaimFourViewModel>()
    private lateinit var claimFourAdapter: ClaimFourAdapter
    private var selectedPosition: Int = -1
    private var mystr: String = ""
    private var serviceCreateModel: ServiceCreate? = null
    private var claimServiceModel: ServiceModel? = null
    private var isEdit: Boolean = false

    var cars: List<Result> = emptyList()
    var strcar: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = FragmentCreateFourclaimBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@CreateClaimFour)
                .get(ClaimFourViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        serviceCreateModel = Gson().fromJson(arguments?.getString("serviceCreateModel"), ServiceCreate::class.java)
        claimServiceModel = Gson().fromJson(arguments?.getString("claimServiceModel"), ServiceModel::class.java)
        if (claimServiceModel != null) isEdit = true
        return ViewModel.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModel.viewmodel?.getCars(Methods.getToken())
        initialise()
    }

    override fun initialise() {
    }

    override fun setObservers() {
        uslug_type_rv.layoutManager = LinearLayoutManager(context)
        uslug_type_rv.setHasFixedSize(true)
        claimFourAdapter = ClaimFourAdapter(ViewModel.viewmodel!!, context as Context)
        uslug_type_rv.adapter = claimFourAdapter


        ViewModel.viewmodel?.cars?.observe(viewLifecycleOwner, Observer {
            claimFourAdapter.updateRepoList(it)
            cars = it
            if (isEdit) {
                it.forEachIndexed { index, car ->
                    if (claimServiceModel?.car?.id.toString() == car.id) {
                        selectedPosition = index
                        strcar = car.id
                    }
                }
                Handler().postDelayed({
                    uslug_type_rv.findViewHolderForAdapterPosition(selectedPosition)?.itemView?.img_check?.visibility = View.VISIBLE
                }, 100)
            }
        })

    }

    override fun setAdapter() {
        val viewModel1 = ViewModel.viewmodel
        if (viewModel1 != null) {
            uslug_type_rv.addOnItemTouchListener(
                RecyclerItemClickListener(uslug_type_rv,
                    object :
                        RecyclerItemClickListener.OnItemClickListener {
                        @SuppressLint("ResourceType")
                        override fun onItemClick(view: View, position: Int) {
                            if (selectedPosition != -1 && selectedPosition != position) {
                                claimFourAdapter.notifyItemChanged(selectedPosition)
                            } else if (selectedPosition == position) {
                                selectedPosition = -1
                            }
                            view.img_check.visibility = View.VISIBLE
                            selectedPosition = position
                            strcar = cars[position].id
                        }
                    })
            )
        }
    }

    override fun setOnClickListener() {
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }

        add_rel.setOnClickListener {
            AddCarBottomDialog {
                viewModel.search()
            }.show(childFragmentManager, "AddCarFromCheckAutoDialog")

        }

        btn_next_claim.setOnClickListener {
            serviceCreateModel?.apply {
                car = strcar.toInt()
                if (isEdit) {
                    claimServiceModel?.id?.let { id-> ViewModel.viewmodel?.editCreate(id, this) }
                } else {
                    ViewModel.viewmodel?.sendCreate(this)
                }
            }
        }

        ViewModel.viewmodel?.favBoldima?.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(R.id.four_main)
            }
        })
        ViewModel.viewmodel?.checkAutoSearch?.observe(viewLifecycleOwner, Observer {
            it.onSuccess {
                ViewModel.viewmodel?.getCars(Methods.getToken())
            }
        })
    }
}
