package com.autonomad.ui.claims.user_claims.home.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.claim_user.Review
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_claims_user_feedback.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class FeedbackAdapter(var reviews: List<Review> = emptyList()) :
    RecyclerView.Adapter<FeedbackAdapter.FeedbackAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackAdapterViewHolder {
        return FeedbackAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_claims_user_feedback,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: FeedbackAdapterViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    fun update(items: List<Review>) {
        reviews = items
        notifyDataSetChanged()
    }

    class FeedbackAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(review: Review) {
            itemView.apply {
                if (!review.profile.avatar.isNullOrEmpty())
                    Picasso.get()
                        .load(review.profile.avatar)
                        .placeholder(R.drawable.ic_profile_male)
                        .into(ivProfileIcon)
                tvUserName.text = review.profile.first_name
                ivStars.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        when (review.star) {
                            1 -> R.drawable.ic_1_star
                            2 -> R.drawable.ic_2_stars
                            3 -> R.drawable.ic_3_stars
                            4 -> R.drawable.ic_4_stars
                            else -> R.drawable.ic_5_stars
                        }
                    )
                )
                tvReviewText.text = review.text
                try {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
                    val reviewCreatedDate = simpleDateFormat.parse(review.created)
                    val prettyTime = PrettyTime(Locale("ru"))
                    reviewCreatedDate?.let { created ->
                        tvReviewCreatedDate.text = prettyTime.format(created)
                    }
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
}