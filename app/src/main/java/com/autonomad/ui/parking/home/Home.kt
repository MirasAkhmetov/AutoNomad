package com.autonomad.ui.parking.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.autonomad.R
import com.autonomad.data.models.parking.CheckPrice
import com.autonomad.data.models.parking.Create_order
import com.autonomad.data.models.parking.pageInfo
import com.autonomad.data.models.payment.CardID
import com.autonomad.data.models.payment.Checkout
import com.autonomad.data.models.payment.ThreeDSFinish
import com.autonomad.databinding.FragmentParkingHomeBinding
import com.autonomad.ui.MainActivity
import com.autonomad.ui.bottom_sheet.banking_cards.BankingCard
import com.autonomad.ui.bottom_sheet.cars.Cars
import com.autonomad.ui.check_auto.report.ThreeDS
import com.autonomad.utils.RecyclerItemClickListener
import com.autonomad.utils.onBackPress
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_parking_home.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import ru.cloudpayments.sdk.cp_card.CPCard
import ru.cloudpayments.sdk.three_ds.ThreeDsDialogFragment
import java.util.*

class Home : Fragment(), ThreeDS, com.autonomad.ui.check_auto.report.BankingCard {
    private lateinit var ViewModel: FragmentParkingHomeBinding
    private val LOCATION_PERMISSION_REQUEST_ID: Int = 777
    private val LOCATION_PERMISSION_SETTING_REQUEST_ID: Int = 778
    val LOC_PERM = Manifest.permission.ACCESS_FINE_LOCATION
    lateinit var picker: SingleDateAndTimePickerDialog.Builder
    private lateinit var locationProvider: FusedLocationProviderClient
    private var location: Location? = null
    private var card_id = CardID("")
    private var orderId: Int? = null

    private lateinit var parkingZonesAdapter: PinAdapter
    private lateinit var cpCard: CPCard
    private lateinit var cardCryptogram: String

    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ViewModel = FragmentParkingHomeBinding.inflate(inflater, container, false).apply {
            viewmodel = ViewModelProvider(this@Home)
                .get(HomeViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return ViewModel.root
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setAdapter()
        setObservers()
        setOnClickListener()
        initialise()
        MainActivity.setListener(this)
        val webView: WebView = view.findViewById(R.id.webView)
        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true;
        webView.loadUrl("file:///android_asset/index.html")
        webView.addJavascriptInterface(this, "Android")

        onBackPress {
            if (main.visibility == View.GONE) {
                parking_pin_rv.visibility = View.GONE
                main.visibility = View.VISIBLE
                (activity as AppCompatActivity).supportActionBar?.show()
                MainActivity.showNav()
                params.setMargins(0, 0, 0, 0)
                searchView.layoutParams = params
                cancel.visibility = View.GONE
            } else {
                findNavController().navigate(R.id.action_global_services)
            }
        }

        bottomSheetBehavior1 = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior1.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetAddBankingCard = BottomSheetBehavior.from(bottom_sheet_add_banking_card)
        bottomSheetAddBankingCard.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun initialise() {
        ViewModel.viewmodel?.initialise()
        ViewModel.viewmodel?.getParkingZones()
        locationProvider = LocationServices.getFusedLocationProviderClient(context as Context)
        val grant = ContextCompat.checkSelfPermission(context as Context, LOC_PERM)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity as Activity,
                arrayOf(LOC_PERM),
                LOCATION_PERMISSION_REQUEST_ID
            )
        } else {
            requestLocation()
        }
        picker = SingleDateAndTimePickerDialog.Builder(context as Context)
            .mainColor(ContextCompat.getColor(context as Context, R.color.PrimaryBlue))
            .bottomSheet()
            .defaultDate(Date(2020, 1, 1, 1, 0))
            .minutesStep(0)
            .displayMinutes(false)
            .displayHours(true)
            .displayAmPm(false)
            .displayDays(false)
            .displayMonth(false)
            .displayYears(false)

        card_number.doOnTextChanged { text, start, count, after ->
            val text = text.toString()
            if (text.endsWith(" "))
                return@doOnTextChanged
            when (card_number.text.length) {
                5 -> {
                    card_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            " "
                        ).toString()
                    )
                    card_number.setSelection(card_number.text.length)
                }
                10 -> {
                    card_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            " "
                        ).toString()
                    )
                    card_number.setSelection(card_number.text.length)
                }
                15 -> {
                    card_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            " "
                        ).toString()
                    )
                    card_number.setSelection(card_number.text.length)
                }
                20 -> {
                    card_number.setText(
                        StringBuilder(text).deleteCharAt(
                            text.length - 1
                        ).toString()
                    )
                    card_number.setSelection(card_number.text.length)
                }
            }
        }
        expire_date_number.doOnTextChanged { text, start, count, after ->
            val text = text.toString()
            if (text.endsWith("/"))
                return@doOnTextChanged
            when (expire_date_number.text.length) {
                3 -> {
                    expire_date_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            "/"
                        ).toString()
                    )
                    expire_date_number.setSelection(expire_date_number.text.length)
                }
                6 -> {
                    expire_date_number.setText(
                        StringBuilder(text).deleteCharAt(
                            text.length - 1
                        ).toString()
                    )
                    expire_date_number.setSelection(expire_date_number.text.length)
                }
            }
        }
        cvv_number.doOnTextChanged { text, start, count, after ->
            val text = text.toString()
            when (cvv_number.text.length) {
                4 -> {
                    cvv_number.setText(
                        StringBuilder(text).deleteCharAt(
                            text.length - 1
                        ).toString()
                    )
                    cvv_number.setSelection(cvv_number.text.length)
                }
            }
        }

    }

    private fun setObservers() {
        ViewModel.viewmodel?.registrated?.observe(viewLifecycleOwner, Observer {
            when (it) {

                true -> {
                    val data = ViewModel.viewmodel?.pageInfo?.value
                    ViewModel.viewmodel?.setData(
                        pageInfo(
                            data?.pin!!,
                            data.hours,
                            data.min,
                            data.bankingCard,
                            card_number.text.toString(),
                            data.price
                        )
                    )
                    bottomSheetBehavior1.state = BottomSheetBehavior.STATE_EXPANDED
                    ViewModel.viewmodel?.registrated?.value = false
                }
            }
        })
        ViewModel.viewmodel?.pageInfo?.observe(viewLifecycleOwner, Observer {
            when {
                it.carNumber.isNotEmpty() && it.bankingCard.isNotEmpty() -> {
                    val data = ViewModel.viewmodel?.pageInfo?.value
                    ViewModel.viewmodel?.checkPrice(
                        CheckPrice(
                            data?.pin!!,
                            data.carNumber,
                            ((data.min.toInt() + (data.hours.toInt() * 60)).toString())
                        )
                    )
                    clickableParkingButton()
                }
                it.carNumber.isNotEmpty() -> {
                    val data = ViewModel.viewmodel?.pageInfo?.value
                    ViewModel.viewmodel?.checkPrice(
                        CheckPrice(
                            data?.pin!!,
                            data.carNumber,
                            ((data.min.toInt() + (data.hours.toInt() * 60)).toString())
                        )
                    )
                    notClickableParkingButton()
                }
                else -> {
                    notClickableParkingButton()
                }
            }
        })
        ViewModel.viewmodel?.parkingZones?.observe(viewLifecycleOwner, Observer {
            parkingZonesAdapter.updateRepoList(it)
        })
        ViewModel.viewmodel?.order?.observe(viewLifecycleOwner, Observer {
            orderId = it.id
            when {
                it.orderId.isNullOrEmpty() -> {
                    ViewModel.viewmodel?.checkStatus(it.id.toString())
                }
                else -> {
                    if (card_id.card_id.isNullOrEmpty()) {
                        ViewModel.viewmodel?.getCredential(it.orderId)
                    } else {
                        ViewModel.viewmodel?.payWithSavedCards(requireView(), it.orderId, card_id)

                    }
                }
            }
        })
        ViewModel.viewmodel?.credential?.observe(viewLifecycleOwner, Observer {
            when {
                it.cp_public_id.length > 1 -> {
                    cpCard = CPCard(
                        card_number.text.toString().replace(" ", ""),
                        expire_date_number.text.toString().replace("/", ""),
                        cvv_number.text.toString()
                    )
                    cardCryptogram = cpCard.cardCryptogram(it.cp_public_id)
                    ViewModel.viewmodel?.payWithNoSavedCars(
                        requireView(),
                        ViewModel.viewmodel?.order?.value?.orderId!!,
                        Checkout(cardCryptogram, "Nursat", "5")
                    )
                }
            }
        })
        ViewModel.viewmodel?.paymentResult?.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                "success" -> {
                    val bundle = bundleOf("id" to orderId.toString(), "payedSuccess" to true)
                    findNavController().navigate(R.id.action_parking_home_to_detailTicketInformation, bundle)
                }
                "3d" -> {
                    ThreeDsDialogFragment.newInstance(
                        it.message.acs_url,
                        it.message.transaction_id,
                        it.message.pa_req
                    ).show(childFragmentManager, "3DS")
                }
            }
        })
        ViewModel.viewmodel?.threeDS?.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    val bundle = bundleOf("id" to orderId, "payedSuccess" to true)
                    findNavController().navigate(R.id.action_parking_home_to_detailTicketInformation, bundle)
                }
                false -> {
                    Snackbar.make(requireView(), "Произошла ошибка", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setAdapter() {
        val viewModel = ViewModel.viewmodel
        if (viewModel != null) {
            parking_pin_rv.layoutManager = LinearLayoutManager(context)
            parkingZonesAdapter = PinAdapter(ViewModel.viewmodel!!)
            parking_pin_rv.adapter = parkingZonesAdapter
            parking_pin_rv.addOnItemTouchListener(
                RecyclerItemClickListener(parking_pin_rv,
                    object :
                        RecyclerItemClickListener.OnItemClickListener {
                        @SuppressLint("ResourceType")
                        override fun onItemClick(view: View, position: Int) {
                            searchView.clearFocus()
                            parking_pin_rv.visibility = View.GONE
                            main.visibility = View.VISIBLE
                            (activity as AppCompatActivity).supportActionBar?.show()
                            MainActivity.showNav()
                            params.setMargins(0, 0, 0, 0)
                            searchView.layoutParams = params
                            cancel.visibility = View.GONE
                            val data = ViewModel.viewmodel?.searchResultParkingZones?.get(position)?.number!!
                            webView.post {
                                webView.evaluateJavascript(
                                    "openBySearch(${data.toInt()});",
                                    null
                                )
                            }
                        }
                    })
            )
        }


    }

    @SuppressLint("SetTextI18n")
    private fun setOnClickListener() {
        parking.setOnClickListener {
            val data = ViewModel.viewmodel?.pageInfo?.value!!
            ViewModel.viewmodel?.createOrder(
                Create_order(
                    data.pin,
                    data.carNumber,
                    (data.min.toInt() + (data.hours.toInt() * 60)).toString()
                )
            )
        }
        KeyboardVisibilityEvent.setEventListener(activity){ isOpen ->
            parking_pin_rv?.visibility = if (isOpen) View.VISIBLE else View.GONE
            cancel?.visibility = if (isOpen) View.VISIBLE else View.GONE
        }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                webView.post {
                    webView.evaluateJavascript(
                        "openBySearch(${query?.toInt()});",
                        null
                    )
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                ViewModel.viewmodel?.parkingZones?.observe(viewLifecycleOwner, Observer {
                    ViewModel.viewmodel?.getSearchParkingZones(it, newText ?: "")?.let { filteredList ->
                        parkingZonesAdapter.updateRepoList(filteredList)
                    }
                })
                return false
            }
        })

        cancel.setOnClickListener {
            searchView.clearFocus()
            parking_pin_rv.visibility = View.GONE
            main.visibility = View.VISIBLE
            (activity as AppCompatActivity).supportActionBar?.show()
            MainActivity.showNav()
            params.setMargins(0, 0, 0, 0)
            searchView.layoutParams = params
            cancel.visibility = View.GONE
            searchView.setQuery("", false)
        }
        zoom_in.setOnClickListener {
            webView.post { webView.evaluateJavascript("zoomIn();", null) }
        }
        zoom_out.setOnClickListener {
            webView.post { webView.evaluateJavascript("zoomOut();", null) }
        }
        my_location.setOnClickListener {
            val grant = ContextCompat.checkSelfPermission(context as Context, LOC_PERM)
            if (grant != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity as Activity,
                    arrayOf(LOC_PERM),
                    LOCATION_PERMISSION_REQUEST_ID
                )
                webView.post { webView.evaluateJavascript("myGeoLocation();", null) }
                askUserToGiveAccessToLocation()
            } else {
                requestLocation()
                askUserToTurnOnLocation()
            }
        }

        time_15_min.setOnClickListener {
            val data = ViewModel.viewmodel?.pageInfo?.value
            ViewModel.viewmodel?.setData(
                pageInfo(
                    data?.pin!!,
                    "0",
                    "15",
                    data.bankingCard,
                    data.carNumber,
                    data.price
                )
            )

            time_15_min.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.grey_3
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.White))
            }

            time_30_min.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.White
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.grey_3))
            }
            time_1_hour.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.White
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.grey_3))
            }
        }
        time_30_min.setOnClickListener {
            val data = ViewModel.viewmodel?.pageInfo?.value
            ViewModel.viewmodel?.setData(
                pageInfo(
                    data?.pin!!,
                    "0",
                    "30",
                    data.bankingCard,
                    data.carNumber,
                    data.price
                )
            )
            time_15_min.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.White
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.grey_3))
            }
            time_30_min.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.grey_3
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.White))
            }
            time_1_hour.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.White
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.grey_3))
            }
        }
        time_1_hour.setOnClickListener {
            val data = ViewModel.viewmodel?.pageInfo?.value
            ViewModel.viewmodel?.setData(
                pageInfo(
                    data?.pin!!,
                    "1",
                    "0",
                    data.bankingCard,
                    data.carNumber,
                    data.price
                )
            )
            time_15_min.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.White
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.grey_3))
            }
            time_30_min.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.White
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.grey_3))
            }
            time_1_hour.apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context as Context,
                        R.color.grey_3
                    )
                )
                setTextColor(ContextCompat.getColor(context as Context, R.color.White))
            }
        }
        time_another.setOnClickListener {
            minutes.visibility = View.GONE
            timers.visibility = View.VISIBLE
        }
        time_1.setOnClickListener {
            picker.displayHours(true)
            picker.displayMinutes(false)
            picker.listener {
                val date = it.toString().split(" ")[3].split(":")
                time_1_text_view.text = date[0] + " час "
                time_2_text_view.text = date[1] + " минут"
                val data = ViewModel.viewmodel?.pageInfo?.value
                ViewModel.viewmodel?.setData(
                    pageInfo(
                        data?.pin!!,
                        date[0],
                        date[1],
                        data.bankingCard,
                        data.carNumber,
                        data.price
                    )
                )
            }
                .display()

        }
        time_2.setOnClickListener {
            picker.displayHours(false)
            picker.displayMinutes(true)
            picker.listener {
                val date = it.toString().split(" ")[3].split(":")
                time_1_text_view.text = date[0] + " час "
                time_2_text_view.text = date[1] + " минут"
                val data = ViewModel.viewmodel?.pageInfo?.value
                ViewModel.viewmodel?.setData(
                    pageInfo(
                        data?.pin!!,
                        date[0],
                        date[1],
                        data.bankingCard,
                        data.carNumber,
                        data.price
                    )
                )
            }
                .display()
        }
        forgotten_parking.setOnClickListener {
            if (parking.text != "Оплатить") {
                parking.text = "Оплатить"
                forgotten_parking.text = "Перейти на обычную парковку"
                time_1_text.visibility = View.INVISIBLE
                time_2_text.visibility = View.INVISIBLE
                ranged_time.visibility = View.GONE
                minutes.visibility = View.GONE
                timers.visibility = View.VISIBLE
            } else {
                default()
            }
        }
        banking_card.setOnClickListener {
            val bankingCard = BankingCard("parking", this)
            bankingCard.show(parentFragmentManager, "")
        }
        car.setOnClickListener {
            val car = Cars(ViewModel.viewmodel!!)
            car.show(parentFragmentManager, "")
        }
        add_banking_card.setOnClickListener {
            if (validate()) {
                val data = ViewModel.viewmodel?.pageInfo?.value
                ViewModel.viewmodel?.setData(
                    pageInfo(
                        data?.pin!!,
                        data.hours,
                        data.min,
                        "******" + card_number.text.split(" ")[3],
                        data.carNumber,
                        data.price
                    )
                )
                card_id.card_id = ""
                bottomSheetAddBankingCard.state = BottomSheetBehavior.STATE_HIDDEN
                bottomSheetBehavior1.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                Snackbar.make(requireView(), "Not validated", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_SETTING_REQUEST_ID, LOCATION_PERMISSION_REQUEST_ID -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation()
                }
            }
        }
    }

    private fun requestLocation() {
        val grant = ContextCompat.checkSelfPermission(context as Context, LOC_PERM)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val request = LocationRequest.create()
        request.interval = 60000
        request.fastestInterval = 20000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val listener = object : LocationCallback() {
            override fun onLocationResult(res: LocationResult?) {
                res?.lastLocation?.let {
                    this@Home.location = it
                }
                val data = location?.latitude.toString() + ":" + location?.longitude.toString()
                try {
                    webView.post { webView.evaluateJavascript("setMyGeoLocation('$data');", null) }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

            }
        }
        locationProvider.requestLocationUpdates(request, listener, null)
    }

    private fun askUserToTurnOnLocation() {
        val lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false
        } catch (ex: Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder(activity as MainActivity)
                .setMessage(getString(R.string.map_turn_on_gps_title))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    activity?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                .setNegativeButton(getString(R.string.no), null)
                .show();
        }
    }

    private fun askUserToGiveAccessToLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity as MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            val grant = ContextCompat.checkSelfPermission(context as Context, LOC_PERM)
            if (grant != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity as Activity,
                    arrayOf(LOC_PERM),
                    LOCATION_PERMISSION_REQUEST_ID
                )
            } else {
                requestLocation()
            }
        } else {
            AlertDialog.Builder(activity as MainActivity)
                .setMessage(getString(R.string.map_turn_on_location_in_settings_title))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri =
                        Uri.fromParts("package", context?.applicationContext?.packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, LOCATION_PERMISSION_SETTING_REQUEST_ID)
                }
                .setNegativeButton(getString(R.string.no), null)
                .show();
        }
    }

    @JavascriptInterface
    fun giveMyGeoLocation(): String {
        return location?.latitude.toString() + ":" + location?.longitude.toString()
    }

    private fun default() {
        minutes.visibility = View.VISIBLE
        timers.visibility = View.GONE
        parking.text = "Припарковаться"
        forgotten_parking.text = "Забыл оплатить парковку"
        timers.visibility = View.GONE
        ranged_time.visibility = View.GONE
        time_1_text.visibility = View.INVISIBLE
        time_2_text.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        searchView.clearFocus()
    }

    @SuppressLint("SetTextI18n")
    @JavascriptInterface
    fun showBottomSheet(pin: String) {
        bottomSheetBehavior1.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior1.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (bottomSheetBehavior1.state) {
                    5, 4 -> {
                        (activity as AppCompatActivity).supportActionBar?.show()
                        top.visibility = View.VISIBLE
                    }
                    3 -> {
                        searchView.setQuery("", false)
                        top.visibility = View.GONE
                        (activity as AppCompatActivity).supportActionBar?.hide()
                        val data = ViewModel.viewmodel?.pageInfo?.value
                        ViewModel.viewmodel?.setData(
                            pageInfo(
                                pin,
                                data?.hours!!,
                                data.min,
                                data.bankingCard,
                                data.carNumber,
                                data.price
                            )
                        )
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

    }

    private fun clickableParkingButton() {
        parking.apply {
            isClickable = true
            isFocusable = true
            isEnabled = true
            background = ContextCompat.getDrawable(
                context as Context,
                R.drawable.background_blue_rectangle
            )
            setTextColor(ContextCompat.getColor(context, R.color.grey_9))
        }
    }

    private fun notClickableParkingButton() {
        parking.apply {
            isClickable = false
            isFocusable = false
            isEnabled = false
            background = ContextCompat.getDrawable(
                context as Context,
                R.drawable.background_grey_8_16dp_rectangle
            )
            setTextColor(ContextCompat.getColor(context, R.color.grey_6))
        }

    }

    private fun validate(): Boolean {
        return CPCard.isValidNumber(
            card_number.text.toString().replace(" ", "")
        ) && CPCard.isValidExpDate(
            expire_date_number.text.toString().replace("/", "")
        ) && cvv_number.text.length == 3
    }


    companion object {
        private lateinit var bottomSheetAddBankingCard: BottomSheetBehavior<View>
        private lateinit var bottomSheetBehavior1: BottomSheetBehavior<View>

        fun showBottomSheetAddBankingCard() {
            bottomSheetBehavior1.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetAddBankingCard.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun success(md: String?, paRes: String?) {
        ViewModel.viewmodel?.threeDSFinish(requireView(), ThreeDSFinish(paRes!!, md!!))
    }

    override fun error() {
        Snackbar.make(requireView(), "Произошла ошибка", Snackbar.LENGTH_LONG).show()
    }

    override fun callFunction(card_info: String) {
        this.card_id = CardID(card_info.split(" ")[0])
        val data = ViewModel.viewmodel?.pageInfo?.value
        ViewModel.viewmodel?.setData(
            pageInfo(
                data?.pin!!,
                data.hours,
                data.min,
                "******" + card_info.split(" ")[1],
                data.carNumber,
                data.price
            )
        )
    }
}
