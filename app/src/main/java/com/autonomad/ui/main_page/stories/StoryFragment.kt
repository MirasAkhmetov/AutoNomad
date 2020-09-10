package com.autonomad.ui.main_page.stories

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.utils.Constants
import com.autonomad.utils.loadImageWithCallback
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.item_story_progress.*

class StoryFragment : Fragment(R.layout.item_story_progress), StoriesProgressView.StoriesListener {

    private lateinit var viewModel: StoriesViewModel// by activityViewModels<MainPageViewModel>()

    private var counter = 0
    private val position by lazy { arguments?.getInt("storyPos") ?: 0 }
    private val resources by lazy { viewModel.stories.value?.item?.list?.get(position)?.snaps ?: emptyList() }

    private var storyPlay = true to true
        set(value) {
            field = value
            if (value.first && value.second) stories_progress.resume()
            else stories_progress.pause()
        }

    private var pressTime = 0L
    private var limit = 500L

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                storyPlay = false to storyPlay.second
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                storyPlay = true to storyPlay.second
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    override fun onComplete() {
        StoriesFragment.nextPage()
    }

    override fun onPrev() {
        if (--counter < 0) {
            counter = resources.size - 1
            return StoriesFragment.previousPage()
        }
        stories_progress.pause()
        image.loadImageWithCallback(Constants.BASE_URL_GARAGE.dropLast(5) + resources[counter].image, onLoadSuccess = {
            stories_progress.resume()
        })
    }

    override fun onNext() {
        if (++counter >= resources.size) {
            counter--
            StoriesFragment.nextPage()
            return
        }
        stories_progress.pause()
        image.loadImageWithCallback(Constants.BASE_URL_GARAGE.dropLast(5) + resources[counter].image, onLoadSuccess = {
            stories_progress.resume()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stories_progress.setStoriesCount(resources.size)
        stories_progress.setStoryDuration(3000L)
        stories_progress.setStoriesListener(this)
        Log.d("StoryFragmentLogcat", "ON_VIEW_CREATED: $parentFragment")

        image.loadImageWithCallback(Constants.BASE_URL_GARAGE.dropLast(5) + resources[counter].image, onLoadSuccess = {
            stories_progress.startStories(counter)
        })

        (parentFragment as? StoriesFragment)?.state?.observe(viewLifecycleOwner) {
            storyPlay = storyPlay.first to it
        }

        reverse.setOnClickListener {
            stories_progress.reverse()
        }
        reverse.setOnTouchListener(onTouchListener)

        skip.setOnClickListener {
            stories_progress.skip()
        }
        skip.setOnTouchListener(onTouchListener)

        ic_exit.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onPause() {
        Log.d("StoryFragmentLogcat", "ON_PAUSE: ")
        super.onPause()
    }

    override fun onResume() {
        Log.d("StoryFragmentLogcat", "ON_RESUME: ")
        super.onResume()
    }

    companion object {
        fun newInstance(position: Int): StoryFragment {
            return StoryFragment().apply { arguments = bundleOf("storyPos" to position) }
        }
    }
}