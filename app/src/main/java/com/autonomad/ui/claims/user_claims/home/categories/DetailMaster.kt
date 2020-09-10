package com.autonomad.ui.claims.user_claims.home.categories

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.data.models.claim_user.MastID
import com.autonomad.data.models.claim_user.MastersId
import com.autonomad.data.models.claim_user.Stars
import com.autonomad.data.models.claims.PluralForms
import com.autonomad.data.models.claims.getPlural
import com.autonomad.databinding.FragmentClaimsUserDetailMasterBinding
import com.autonomad.ui.bottom_sheet.claim.SmskaBottomDialog
import com.autonomad.ui.claims.user_claims.home.mymasters.CustomExpandableListAdapter
import com.autonomad.ui.claims.user_claims.home.mymasters.DetailMasterModelView
import com.autonomad.utils.BaseFragment
import com.autonomad.utils.Constants
import com.autonomad.utils.Methods
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_claims_user_detail_master.*
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*


class DetailMaster : BaseFragment() {
    private val viewModel: DetailMasterModelView by viewModels()
    private val listOfImagesAdapter = ImageAdapter()
    private var isFav: Boolean = false

    private lateinit var master: MastersId

    private val feedbackAdapter = FeedbackAdapter()
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null
    val data: HashMap<String, List<String>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentClaimsUserDetailMasterBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMastersId(
            Methods.getToken(),
            arguments?.getString("idd").toString()
        )
        viewModel.loadReviews(
            Methods.getToken(),
            arguments?.getString("idd").toString()
        )
    }

    override fun initialise() {}

    class ChildTwo {
        var price: String? = null
        var subCategory: String? = null
    }

    override fun setObservers() {
        viewModel.serviceImages.observe(viewLifecycleOwner, Observer {
            listOfImagesAdapter.serviceimages = it
        })

        viewModel.mastersId.observe(viewLifecycleOwner, Observer {
            isFav = it.is_fav
            master = it

            iv_address_icon.visibility = if (it.address_name.isEmpty()) View.GONE else View.VISIBLE

            text_age.text = getAgeFromBirthDate(it.profile.dateOfBirth) ?: ""

            if (it.profile.avatar?.isEmpty() == true) {
                ic_user.setImageResource(R.drawable.ic_profile_user_icon)
            } else {
                Picasso.get()
                    .load(Constants.BASE_URL_IMAGES.plus(it.profile.avatar))
                    .placeholder(R.drawable.ic_profile_user_icon)
                    .into(ic_user)
            }

            val listData = HashMap<String, List<ChildTwo>>()

            for (i in it.offers.indices) {
                val children = ArrayList<ChildTwo>()

                val replyArr = it.offers[i].sub_categories
                for (j in replyArr.indices) {
                    val childTwo = ChildTwo()
                    childTwo.price = replyArr[j].price
                    childTwo.subCategory = replyArr[j].sub_category
                    children.add(childTwo)
                }
                listData[it.offers[i].category] = children

            }
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(requireContext(), titleList as ArrayList<String>, listData)
            expandableListViewka.setAdapter(adapter)

            text_services_list_empty.visibility = if (titleList.isNullOrEmpty()) View.VISIBLE else View.GONE

            setExpandableListViewHeight(expandableListViewka)
            expandableListViewka.setOnGroupClickListener { parent, _, groupPosition, _ ->
                setListViewHeight(parent, groupPosition)
                false
            }

            setReviewProgresses(it.stars)
        })

        viewModel.reviews.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                see_all.isVisible = it.size > 3
                feedbackAdapter.update(it.take(3))
            }
        })
    }

    private fun setListViewHeight(listView: ExpandableListView, group: Int) {
        val listAdapter = listView.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            listView.width,
            View.MeasureSpec.EXACTLY
        )
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight
            if (listView.isGroupExpanded(i) && i != group
                || !listView.isGroupExpanded(i) && i == group
            ) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
                //Add Divider Height
                totalHeight += listView.dividerHeight * (listAdapter.getChildrenCount(i) - 1)
            }
        }
        //Add Divider Height
        totalHeight += listView.dividerHeight * (listAdapter.groupCount - 1)
        val params = listView.layoutParams
        var height = (totalHeight
                + listView.dividerHeight * (listAdapter.groupCount - 1))
        if (height < 10) height = 200
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

    private fun setExpandableListViewHeight(listView: ExpandableListView) {
        try {
            val listAdapter = listView.expandableListAdapter as ExpandableListAdapter
            var totalHeight = 0
            for (i in 0 until listAdapter.groupCount) {
                val listItem = listAdapter.getGroupView(i, false, null, listView)
                listItem.measure(0, 0)
                totalHeight += listItem.measuredHeight
            }
            val params = listView.layoutParams
            var height = totalHeight + listView.dividerHeight * (listAdapter.groupCount - 1)
            if (height < 10) height = 200
            params.height = height
            listView.layoutParams = params
            listView.requestLayout()
            scrollll.post { scrollll.fullScroll(ScrollView.FOCUS_UP) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setOnClickListener() {
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
        see_all.setOnClickListener {
            val bundle = bundleOf(
                "idd" to arguments?.getString("idd").toString(),
                "stars" to Gson().toJson(master.stars),
                "star_avg" to master.star_avg,
                "review_count" to master.review_count
            )
            findNavController().navigate(R.id.action_detailMaster_to_feedback, bundle)
        }

        ic_receipt.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        ic_smsdetail.setOnClickListener {
            val car = SmskaBottomDialog(R.id.action_penalty_to_penaltiesOfPerson, master.id)
            car.show(parentFragmentManager, "AddSms")
        }

        ic_serdse.setOnClickListener() {
            if (!isFav) {
                isFav = true
                ic_serdse.setImageResource(R.drawable.ic_claims_favourites)
                viewModel.postFavour(
                    Methods.getToken(),
                    arguments?.getString("idd").toString(),
                    MastID(arguments?.getString("idd").toString())
                )
            } else {
                isFav = false
                ic_serdse.setImageResource(R.drawable.ic_baseline_favorite_24)
                viewModel.deleteFavour(
                    Methods.getToken(),
                    arguments?.getString("idd").toString()
                )
            }
        }

        btnCall.setOnClickListener {
            try {
                activity?.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${master.profile.phone}")))
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
        btn_chat.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Функция мессенджера появится скоро")
                .setNegativeButton("Понятно") { _, _ -> }
                .show()
        }
    }

    override fun setAdapter() {
        images_rv.adapter = listOfImagesAdapter
        rv_reviews.adapter = feedbackAdapter
    }

    private fun getAgeFromBirthDate(birthDate: String): String? {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = simpleDateFormat.parse(birthDate)
        formattedDate?.let {
            val dobCalendar = Calendar.getInstance().apply {
                time = formattedDate
            }
            val today = Calendar.getInstance()
            var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            return getPlural(age, PluralForms("год", "года", "лет"), false)

        }
        return ""
    }

    private fun setReviewProgresses(stars: Stars) {
        var sum = 0
        sum += stars.one ?: 0
        sum += stars.two ?: 0
        sum += stars.three ?: 0
        sum += stars.four ?: 0
        sum += stars.five ?: 0

        progress_horizontal1.progress = (((stars.one ?: 0).toFloat() / sum) * 100).toInt()
        progress_horizontal2.progress = (((stars.two ?: 0).toFloat() / sum) * 100).toInt()
        progress_horizontal3.progress = (((stars.three ?: 0).toFloat() / sum) * 100).toInt()
        progress_horizontal4.progress = (((stars.four ?: 0).toFloat() / sum) * 100).toInt()
        progress_horizontal5.progress = (((stars.five ?: 0).toFloat() / sum) * 100).toInt()
    }
}