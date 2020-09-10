package com.autonomad.ui.profile

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.login.Profile
import com.autonomad.ui.main_page.stories.CubeTransformer
import com.autonomad.utils.Methods
import com.autonomad.utils.navigateBack
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel>()
    private var profile: Profile? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateBack(R.id.action_global_main_page)

        viewModel.getProfile(Methods.getToken())

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            profile = it
            text_name_surname.text = "${it.lastName}  ${it.firstName}".trim()
            phone_number.text = it.phone
            email.text = it.email
            if (it.thumbnail == null && it.avatar == null) {
                profile_icon.setImageResource(R.drawable.ic_profile_male)
            } else if (it.thumbnail != null) {
                Picasso.get()
                    .load(it.thumbnail)
                    .placeholder(R.drawable.ic_profile_male)
                    .into(profile_icon)
            } else if (it.avatar != null) {
                Picasso.get()
                    .load(it.avatar)
                    .placeholder(R.drawable.ic_profile_male)
                    .into(profile_icon)
            }
        })

        link_to_profile_page.setOnClickListener {
            val bundle = bundleOf("profile" to Gson().toJson(profile))
            findNavController().navigate(R.id.action_profile_to_userPageFragment, bundle)
        }
        setViewPager()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.link_to_settings) {
            val bundle = bundleOf("idd" to (profile?.id ?: 0))
            findNavController().navigate(R.id.action_profile_to_settingsPage, bundle)
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun setViewPager() {
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        val viewPagerAdapter = ProfileAdapter(childFragmentManager, tabLayout.tabCount)
        pager.setPageTransformer(true, CubeTransformer())
        pager.adapter = viewPagerAdapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setupWithViewPager(pager)
        tabLayout.getTabAt(0)?.text = "Автомобили"
        tabLayout.getTabAt(1)?.text = "Водители"
    }
}