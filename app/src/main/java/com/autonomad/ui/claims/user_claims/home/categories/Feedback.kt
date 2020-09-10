package com.autonomad.ui.claims.user_claims.home.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.claim_user.Review
import com.autonomad.data.models.claim_user.Stars
import com.autonomad.data.models.claims.ReviewFilter
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.Methods
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_claims_user_master_feedbacks.*
import kotlinx.android.synthetic.main.fragment_claims_user_master_feedbacks.progress_horizontal1
import kotlinx.android.synthetic.main.fragment_claims_user_master_feedbacks.progress_horizontal2
import kotlinx.android.synthetic.main.fragment_claims_user_master_feedbacks.progress_horizontal3
import kotlinx.android.synthetic.main.fragment_claims_user_master_feedbacks.progress_horizontal4
import kotlinx.android.synthetic.main.fragment_claims_user_master_feedbacks.progress_horizontal5

class Feedback : BaseFragment() {
    private val viewModel by viewModels<ReviewsViewModel>()
    private val reviews = ArrayList<Review>()
    private lateinit var feedbackAdapter: FeedbackAdapter
    private lateinit var feedbackTypeAdapter: FeedbackTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_claims_user_master_feedbacks, container, false)
    }

    override fun initialise() {
        val masterId = arguments?.getString("idd").toString()
        val stars = Gson().fromJson(arguments?.getString("stars").toString(), Stars::class.java)
        val starAvg = arguments?.getDouble("star_avg")
        val reviewCount = arguments?.getInt("review_count")

        viewModel.getReviews(Methods.getToken(), masterId)
        setStatistics(stars, reviewCount, starAvg)
        iv_back.setOnClickListener { activity?.onBackPressed() }
    }

    override fun setObservers() {
        viewModel.reviews.observe(viewLifecycleOwner, Observer {
            reviews.addAll(it)
            feedbackAdapter.update(it)
        })
    }

    override fun setOnClickListener() {

    }

    override fun setAdapter() {
        val types = ArrayList<ReviewFilter>();
        types.add(ReviewFilter(0, "Все отзывы").apply { isSelected = true; type = ReviewFilter.Types.ALL })
        types.add(ReviewFilter(1, "Положительные").apply { isSelected = false; type = ReviewFilter.Types.POSITIVE })
        types.add(ReviewFilter(2, "Отрицательные").apply { isSelected = false; type = ReviewFilter.Types.NEGATIVE })
        types.add(ReviewFilter(3, "Нейтральные").apply { isSelected = false; type = ReviewFilter.Types.NEUTRAL })

        feedback_type_rv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        feedbackTypeAdapter = FeedbackTypeAdapter(types, ::onFilterSelected)
        feedback_type_rv.adapter = feedbackTypeAdapter

        feedback_rv.layoutManager = LinearLayoutManager(context)
        feedbackAdapter = FeedbackAdapter(reviews)
        feedback_rv.adapter = feedbackAdapter
    }

    private fun setStatistics(stars: Stars, reviewCount: Int?, starAvg: Double?) {
        setReviewProgresses(stars)
        reviewCount?.let { tvReviewCount.text = it.toString().plus(" отзывов")}
        tvStarAvg.text = starAvg.toString()
    }

    private fun setReviewProgresses(stars: Stars) {
        var sum = 0
        sum += stars.one ?: 0
        sum += stars.two ?: 0
        sum += stars.three ?: 0
        sum += stars.four ?: 0
        sum += stars.five ?: 0

        tvCountStar1.text = stars.one.toString()
        tvCountStar2.text = stars.two.toString()
        tvCountStar3.text = stars.three.toString()
        tvCountStar4.text = stars.four.toString()
        tvCountStar5.text = stars.five.toString()

        progress_horizontal1.progress =  (((stars.one ?: 0).toFloat() / sum) * 100).toInt()
        progress_horizontal2.progress = (((stars.two ?: 0).toFloat() / sum) * 100).toInt()
        progress_horizontal3.progress = (((stars.three ?: 0).toFloat() / sum) * 100).toInt()
        progress_horizontal4.progress = (((stars.four ?: 0).toFloat() / sum) * 100).toInt()
        progress_horizontal5.progress = (((stars.five ?: 0).toFloat() / sum) * 100).toInt()
    }

    private fun onFilterSelected(position: Int, type: ReviewFilter.Types) {
        feedbackTypeAdapter.updateItems(position)
        filterReviews(type)
    }

    private fun filterReviews(type: ReviewFilter.Types) {
        if (type == ReviewFilter.Types.ALL) {
            iv_no_responses.visibility = View.GONE
            feedbackAdapter.update(reviews)
        } else {
            val filteredReviews = ArrayList<Review>()
            reviews.forEach {
                val ff = when (it.star) {
                    1, 2 -> ReviewFilter.Types.NEGATIVE
                    3 -> ReviewFilter.Types.NEUTRAL
                    4, 5 -> ReviewFilter.Types.POSITIVE
                    else -> ReviewFilter.Types.ALL
                }
                if (ff == type) {
                    filteredReviews.add(it)
                }
            }
            iv_no_responses.visibility = if (filteredReviews.size > 0) View.GONE else View.VISIBLE
            feedbackAdapter.update(filteredReviews)
        }
    }
}
