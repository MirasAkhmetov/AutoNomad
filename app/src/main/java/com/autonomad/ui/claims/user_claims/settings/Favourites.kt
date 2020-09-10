package com.autonomad.ui.claims.user_claims.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.databinding.FragmentClaimsUserFavouritesBinding
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.Methods
import kotlinx.android.synthetic.main.fragment_claims_user_favourites.*

class Favourites : BaseFragment(){
    private lateinit var ViewModel: FragmentClaimsUserFavouritesBinding
    private lateinit var masters_adapter: FavAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = FragmentClaimsUserFavouritesBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@Favourites)
                .get(FavouritesViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return ViewModel.root


    }

    override fun initialise() {
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    override fun setOnClickListener() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModel.viewmodel?.getFubCatt(Methods.getToken(), arguments?.getString("idd").toString())
    }

    override fun setObservers() {
        ViewModel.viewmodel?.subMasterList?.observe(viewLifecycleOwner, Observer {
            masters_adapter.updateRepoList(it)
        })
    }

    override fun setAdapter() {
        val viewModel = ViewModel.viewmodel
        if (viewModel != null) {
            favourites_rv.layoutManager = LinearLayoutManager(context)
            favourites_rv.setHasFixedSize(true)
            masters_adapter = FavAdapter(ViewModel.viewmodel!!, context as Context)
            favourites_rv.adapter = masters_adapter

        }

//        favourites_rv.addOnItemTouchListener(
//            RecyclerItemClickListener(favourites_rv
//                ,
//                object :
//                    RecyclerItemClickListener.OnItemClickListener {
//                    @SuppressLint("ResourceType")
//                    override fun onItemClick(view: View, position: Int) {
//                        findNavController().navigate(R.id.action_favourites_to_rate)
//                    }
//                })
//        )

    }
}

