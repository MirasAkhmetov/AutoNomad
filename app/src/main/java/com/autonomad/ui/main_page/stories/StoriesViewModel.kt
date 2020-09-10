package com.autonomad.ui.main_page.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import com.autonomad.data.models.Story
import com.autonomad.utils.*

// !НЕ ТРОГАТЬ
abstract class StoriesViewModel : DisposableViewModel() {
    protected val mStories = MutableLiveData<Status<Page<Story>>>()
    val stories: LiveData<Status<Page<Story>>> = mStories
}