package com.autonomad.ui.claims.specialist_claims.settings.statistics

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.claims.SpecialistStatistics
import com.autonomad.utils.daysOfWeek
import kotlinx.android.synthetic.main.fragment_claims_specialist_statistics.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class StatisticsFragment : Fragment(R.layout.fragment_claims_specialist_statistics) {
    private val TAG = "StatFragmentLogcat"
    private val viewModel by viewModels<StatisticsViewModel>()

    private val viewTextViews by lazy {
        listOf(tv_view_day_1, tv_view_day_2, tv_view_day_3, tv_view_day_4, tv_view_day_5, tv_view_day_6, tv_view_day_7)
    }
    private val viewCards by lazy {
        listOf(
            card_view_day_1,
            card_view_day_2,
            card_view_day_3,
            card_view_day_4,
            card_view_day_5,
            card_view_day_6,
            card_view_day_7
        )
    }
    private val viewsCountTextViews by lazy {
        listOf(
            tv_view_count_day_1,
            tv_view_count_day_2,
            tv_view_count_day_3,
            tv_view_count_day_4,
            tv_view_count_day_5,
            tv_view_count_day_6,
            tv_view_count_day_7
        )
    }
    private val responseTextViews by lazy {
        listOf(
            tv_responses_day_1,
            tv_responses_day_2,
            tv_responses_day_3,
            tv_responses_day_4,
            tv_responses_day_5,
            tv_responses_day_6,
            tv_responses_day_7
        )
    }
    private val responseCards by lazy {
        listOf(
            card_responses_day_1,
            card_responses_day_2,
            card_responses_day_3,
            card_responses_day_4,
            card_responses_day_5,
            card_responses_day_6,
            card_responses_day_7
        )
    }
    private val responsesCountTextViews by lazy {
        listOf(
            tv_responses_count_day_1,
            tv_responses_count_day_2,
            tv_responses_count_day_3,
            tv_responses_count_day_4,
            tv_responses_count_day_5,
            tv_responses_count_day_6,
            tv_responses_count_day_7
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCards[0].layoutParams.height = 50
        viewModel.statistics.observe(viewLifecycleOwner) {
            it.onSuccess { setViews(this) }
            it.onFailure { Toast.makeText(context, this, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun setViews(statistics: SpecialistStatistics) {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val views = statistics.viewCount.map { format.parse(it[0]) to (it[1].toIntOrNull() ?: 0) }.sortedBy { it.first }
        val responses =
            statistics.serviceResponseCount.map { format.parse(it[0]) to (it[1].toIntOrNull() ?: 0) }.sortedBy { it.first }
        val maxViews = views.maxBy { it.second }?.second?.toDouble() ?: 0.0
        val maxResponses = responses.maxBy { it.second }?.second?.toDouble() ?: 0.0
        val c = Calendar.getInstance()
        for (i in 0 until 7) {
            c.time = views[i].first ?: c.time
            viewTextViews[i].text = daysOfWeek[c.get(Calendar.DAY_OF_WEEK) - 1]
            val layoutParams = viewCards[i].layoutParams
            viewsCountTextViews[i].text = views[i].second.toString()
            layoutParams.height = dpToPx(15 + (85 * views[i].second / maxViews).toInt())
            viewCards[i].layoutParams = layoutParams

            c.time = responses[i].first ?: c.time
            responseTextViews[i].text = daysOfWeek[c.get(Calendar.DAY_OF_WEEK) - 1]
            responseCards[i].layoutParams.height = dpToPx(15 + 51 * (responses[i].second / maxResponses).toInt())
            responsesCountTextViews[i].text = responses[i].second.toString()
        }

        val viewsCount = views.sumBy { it.second }
        tv_views_count_weekly.text = getCountString(viewsCount, "просмотр")
        val responsesCount = responses.sumBy { it.second }
        tv_responses_count_weekly.text = getCountString(responsesCount, "отклик")
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).roundToInt()
    }

    private fun getCountString(count: Int, item: String) = "$count " + (if (count % 100 / 10 != 1)
        when {
            count % 10 == 1 -> item
            count % 10 in 2..4 -> "${item}а"
            else -> "${item}ов"
        } else "${item}ов") + " на этой неделе"
}