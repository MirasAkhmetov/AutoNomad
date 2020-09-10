package com.autonomad.ui.main_page.stories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.autonomad.R


class StoriesFragment : Fragment(R.layout.fragment_stories) {

    private lateinit var viewModel: StoriesViewModel //by activityViewModels<MainPageViewModel>()
    private val mState = MutableLiveData(true)
    val state: LiveData<Boolean> = mState

    companion object {
        private lateinit var pager: ViewPager

        fun nextPage() {
            pager.currentItem = pager.currentItem + 1
        }

        fun previousPage() {
            pager.currentItem = pager.currentItem - 1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = StoriesAdapter(childFragmentManager, viewModel.stories.value?.item?.list ?: emptyList())
        pager = view.findViewById(R.id.pager) as ViewPager
        pager.setPageTransformer(true, CubeTransformer())
        pager.adapter = adapter
        pager.offscreenPageLimit = 2
        val position = arguments?.getInt("position")
        if (position != null && position != 0)
            pager.setCurrentItem(position, false)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                mState.value = state == ViewPager.SCROLL_STATE_IDLE
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {}
        })
    }
}