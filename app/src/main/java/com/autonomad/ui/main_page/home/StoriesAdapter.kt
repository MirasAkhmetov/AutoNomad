package com.autonomad.ui.main_page.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.Story
import com.autonomad.utils.loadImage
import kotlinx.android.synthetic.main.item_story.view.*

// !НЕ ТРОГАТЬ
class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder>() {
    var stories = emptyList<Story>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoriesViewHolder(root)
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        holder.bind(stories[position], position)
    }

    class StoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(story: Story, position: Int) {
            itemView.story_tag.text = story.name
            itemView.iv_story.loadImage(story.image)
            ViewCompat.setTransitionName(itemView, "${story.name}$position")
        }
    }

}