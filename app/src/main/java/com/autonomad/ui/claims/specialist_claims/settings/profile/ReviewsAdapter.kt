package com.autonomad.ui.claims.specialist_claims.settings.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.data.models.claims.Review
import com.autonomad.databinding.ItemClaimSpecialistReviewBinding

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {
    var reviews: List<Review> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        ItemClaimSpecialistReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    class ReviewViewHolder(private val binding: ItemClaimSpecialistReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        private var expanded = false
            set(value) {
                field = value
                binding.tvReviewText.maxLines = if (value) Int.MAX_VALUE else 3
            }

        fun bind(item: Review) {
            binding.review = item
            expanded = false
            binding.tvReviewText.setOnClickListener {
                expanded = !expanded
            }
        }
    }
}

@BindingAdapter("starsDrawable")
fun setStarsDrawable(view: ImageView, @DrawableRes id: Int) {
    view.setImageResource(id)
}