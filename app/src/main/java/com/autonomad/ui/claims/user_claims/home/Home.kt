package com.autonomad.ui.claims.user_claims.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.databinding.FragmentClaimsUserHomeBinding
import com.autonomad.utils.Methods
import com.autonomad.utils.navigateBack
import com.synnapps.carouselview.CarouselView
import kotlinx.android.synthetic.main.fragment_claims_user_home.*

class Home : Fragment() {
    private lateinit var ViewModel: FragmentClaimsUserHomeBinding
    private lateinit var categoryAdapter: CategoriesAdapter
    private val mastersAdapter = MastersAdapter()
    private var sampleImages =
        intArrayOf(R.drawable.ic_banner1, R.drawable.ic_banner2, R.drawable.ic_banner3)
    private lateinit var carousel: CarouselView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = FragmentClaimsUserHomeBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@Home)
                .get(ClaimHomeViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return ViewModel.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        setOnClickListener()
        setAdapter()
        setObservers()
        ViewModel.viewmodel?.getMasters(Methods.getToken())
        ViewModel.viewmodel?.getCategories(Methods.getToken())

    }

    private fun setObservers() {
        ViewModel.viewmodel?.categories?.observe(viewLifecycleOwner, Observer {
            categoryAdapter.updateRepoList(it)
        })
        ViewModel.viewmodel?.masters?.observe(viewLifecycleOwner, Observer {
            mastersAdapter.updateRepoList(it)
        })
    }

    private fun setAdapter() {
        ViewModel.viewmodel?.let {
            masters_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            masters_rv.adapter = mastersAdapter

            categoryAdapter = CategoriesAdapter()
            rv_categories.adapter = categoryAdapter
        }
    }

    private fun setOnClickListener() {
        add.setOnClickListener {
            findNavController().navigate(R.id.action_claim_user_home_to_claimuser)
        }
    }


    private fun initialise() {
        navigateBack(R.id.action_global_services)

        carousel = view?.findViewById(R.id.carouselView) as CarouselView
        carousel.setImageListener { position, imageView ->
            imageView.setImageResource(sampleImages[position])
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        }
        carousel.pageCount = sampleImages.size;
    }

}