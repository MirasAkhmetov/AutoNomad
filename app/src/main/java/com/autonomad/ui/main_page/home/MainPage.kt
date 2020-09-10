package com.autonomad.ui.main_page.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.NotificationsService
import com.autonomad.R
import com.autonomad.data.models.meteo_currency.City
import com.autonomad.data.models.meteo_currency.Currency
import com.autonomad.data.models.meteo_currency.Weather
import com.autonomad.databinding.FragmentMainPageBinding
import com.autonomad.ui.BottomNavMenu
import com.autonomad.ui.bottom_sheet.AddCarBottomDialog
import com.autonomad.ui.bottom_sheet.insurance.CheckPolis
import com.autonomad.ui.bottom_sheet.main_page.*
import com.autonomad.ui.bottom_sheet.penalty.AddDriverBottomDialog
import com.autonomad.ui.claims.specialist_claims.feedback.Feedback
import com.autonomad.ui.main_page.tech_inspection.TechInspectionFragment
import com.autonomad.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_main_page.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

class MainPage : NavigationFragment(), MainPageClickHandler, BottomDialogListener {
    private val viewModel: MainPageViewModel by viewModel()
    private lateinit var fuelsAdapter: FuelAdapter
    private lateinit var fusedLocation: FusedLocationProviderClient

    private var error: String = ""
        set(value) {
            if (field != value) {
                field = value
                toast(value)
            }
        }

    private val networkObserver by lazy {
        Observer<Boolean> {
            if (it) {
                setNetworkValueVisibility(tv_network_error, true)
                viewModel.loadData(true)
                getLocation()
            } else {
                setNetworkValueVisibility(tv_network_error, false)
                getLocalCurrencies()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 523
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainPageBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
            clickHandler = this@MainPage
        }
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPress { activity?.finish() }
        getLocation()
        viewModel.loadData()
        swipe_layout.setOnRefreshListener {
            viewModel.loadData(true)
        }
        setObservers()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getLocation()
    }

    override fun onRefresh() {
        if (refreshIndicators[MainPageItem.INSPECTION] == true && refreshIndicators[MainPageItem.INSURANCE] == true && refreshIndicators[MainPageItem.PENALTIES] == true) return
        viewModel.loadData(true)
        refreshIndicators[MainPageItem.INSURANCE] = true
        refreshIndicators[MainPageItem.PENALTIES] = true
        refreshIndicators[MainPageItem.INSPECTION] = true
        startRefreshAnimation(iv_refresh_inspection, MainPageItem.INSPECTION)
        startRefreshAnimation(iv_refresh_insurance, MainPageItem.INSURANCE)
        startRefreshAnimation(iv_refresh_penalties, MainPageItem.PENALTIES)
    }

    override fun onInternetErrorClick() {
        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
    }

    override fun onAddCar() {
        AddCarBottomDialog { viewModel.loadData(true) }.show(childFragmentManager, "AddCar")
    }

    override fun onInsuranceClick() {
        if (viewModel.insurance.value?.item?.isNotEmpty() == true)
            InsuranceBottomDialog(viewModel.insurance, this).show(
                childFragmentManager,
                "InsuranceBottomDialog"
            )
        else onNewClick(MainPageItem.INSURANCE)
    }

    override fun onPenaltiesClick() {
        if (viewModel.drivers.value?.item?.isNotEmpty() == true)
            PenaltiesBottomDialog(viewModel.drivers, this).show(
                childFragmentManager,
                "PenaltiesBottomDialog"
            )
        else onNewClick(MainPageItem.PENALTIES)
    }

    override fun onInspectionClick() {
        if (viewModel.garageCars.value?.item?.isNotEmpty() == true)
            InspectionBottomDialog(viewModel.garageCars, viewModel.inspections, this).show(
                childFragmentManager,
                "InspectionsBottomDialog"
            )
        else onNewClick(MainPageItem.INSPECTION)
    }

    override fun onServicesClick(serviceId: Int) {
        findNavController().navigate(
            R.id.action_main_page_to_category_list,
            bundleOf("idd" to "$serviceId")
        )
    }

    override fun onNewClick(item: MainPageItem) {
        when (item) {
            MainPageItem.INSURANCE -> {
                CheckPolis.show(childFragmentManager)
            }
            MainPageItem.PENALTIES -> {
                AddDriverBottomDialog(R.id.action_main_page_to_penalties_of_person).show(
                    childFragmentManager,
                    "AddDriver"
                )
            }
            MainPageItem.INSPECTION -> {
                AddInspectionBottomDialog { stateNumber, srts ->
                    findNavController().navigate(
                        R.id.action_main_page_to_tech_inspection,
                        bundleOf("stateNumber" to stateNumber, "srts" to srts)
                    )
                }.show(childFragmentManager, "AddInspection")
            }
        }
    }

    override fun onItemClick(item: MainPageItem, id: Int) {
        when (item) {
            MainPageItem.INSURANCE -> {
                findNavController().navigate(
                    R.id.action_main_page_to_insurance_page,
                    bundleOf("id" to id)
                )
            }
            MainPageItem.PENALTIES -> {
                val driver = viewModel.drivers.value?.item?.firstOrNull { it.id == id }
                if (driver != null)
                    findNavController().navigate(
                        R.id.action_main_page_to_penalties_of_person,
                        bundleOf("UIN" to driver.target)
                    )
            }
            MainPageItem.INSPECTION -> {
                val car = viewModel.garageCars.value?.item?.firstOrNull { it.id == id } ?: return
                if (car.srts.isEmpty()) {
                    AddSrtsBottomDialog(car.markModel) { srts ->
                        viewModel.updateCar(car, srts)
                    }.show(childFragmentManager, "AddSrtsBottomDialog")
                } else {
                    findNavController().navigate(
                        R.id.action_main_page_to_tech_inspection,
                        bundleOf(TechInspectionFragment.CAR_ID to id)
                    )
                }
            }
        }
    }

    private fun setObservers() {
        navigationViewModel.isConnected.observe(viewLifecycleOwner, networkObserver)
        when (navigationViewModel.getNotificationExtra(NotificationsService.PUSH_NOTIFICATION_TYPE)) {
            "insurance_checked" -> {
                navigationViewModel.setMenu(BottomNavMenu.InsuranceMenu)
                findNavController().navigate(
                    R.id.action_main_page_to_insurance_home,
                    bundleOf("toHistory" to true)
                )
            }
            "service_offer" -> {
                Methods.setSpecialist(false)
                navigationViewModel.setMenu(BottomNavMenu.ClaimsUserMenu)
                findNavController().navigate(R.id.action_main_page_to_user_claims)
            }
            "service_request" -> {
                Methods.setSpecialist(true)
                navigationViewModel.setMenu(BottomNavMenu.ClaimsSpecialistMenu)
                findNavController().navigate(
                    R.id.action_main_page_to_claims_specialist_responses,
                    bundleOf(Feedback.SELECTED_TAB to 1)
                )
            }
            "penalties_detected" -> {
                navigationViewModel.setMenu(BottomNavMenu.PenaltiesMenu)
                findNavController().navigate(R.id.action_main_page_to_penalties)
//                todo get UIN from back or navigate to penalties home
            }
        }
        viewModel.profile.observe(viewLifecycleOwner) { status ->
            status.onFail {
                error = it
            }
        }
        viewModel.drivers.observe(viewLifecycleOwner) { status ->
            if (!status.isLoading) {
                swipe_layout.isRefreshing = false
                refreshIndicators[MainPageItem.PENALTIES] = false
            }
            status.onSuccess {
                val penaltiesCount = this.sumBy { item -> item.penaltyCount }
                val (image, color) = if (penaltiesCount > 0)
                    R.drawable.ic_bad to R.color.situational_red_error
                else
                    R.drawable.ic_good to R.color.grey_3
                iv_penalties_status.setImageResource(image)
                tv_penalties.setTextColor(ContextCompat.getColor(requireContext(), color))
                tv_penalties.text =
                    if (penaltiesCount > 0) "обнаружено $penaltiesCount" else "все чисто"
            }
            status.onFail {
                if (!it.contains("подключение к сети")) error = it
                else {
                    navigationViewModel.isConnected.observe(viewLifecycleOwner, networkObserver)
                }
                timber("penalties: $it")
            }
        }
        viewModel.insurance.observe(viewLifecycleOwner) { status ->
            if (!status.isLoading) {
                refreshIndicators[MainPageItem.INSURANCE] = false
                swipe_layout.isRefreshing = false
            }
            status.onSuccess {
                val i =
                    filter { i -> i.insuranceCheck.endDate != null }.sortedBy { i -> i.insuranceCheck.endDate }
                        .firstOrNull()
                formatDate(
                    tv_insurance_period,
                    i?.insuranceCheck?.endDate,
                    "dd.MM.yyyy",
                    null,
                    "до "
                )
                if (size > 1) tv_insurance_extra.text = getString(R.string.more, size - 1)
                Methods.showError("insurance", true)
            }
            status.onFail {
                if (!it.contains("подключение к сети") && Methods.showError("insurance")) {
                    toast(it)
                    Methods.showError("insurance", false)
                }
                timber("insurance: $it")
            }
        }
        viewModel.inspections.observe(viewLifecycleOwner) { status ->
            if (!status.isLoading) {
                swipe_layout.isRefreshing = false
                refreshIndicators[MainPageItem.INSPECTION] = false
            }
            status.onSuccess {
                minBy { i -> SimpleDateFormat("yyyy-MM-dd").parse(i.expirationDate) }?.let { i ->
                    formatDate(tv_inspection_period, i.expirationDate, "dd.MM.yyyy", null, "до")
                }
                tv_inspection_period_extra.text = getString(R.string.more, size - 1)
                Methods.showError("inspections", true)
            }
            status.onFail {
                if (!it.contains("подключение к сети") && Methods.showError("inspections")) {
                    toast(it)
                    Methods.showError("inspections", false)
                }
                timber("inspections: $it")
            }
        }
        viewModel.updateSrts.observe(viewLifecycleOwner) { status ->
            status.onSuccess {
                findNavController().navigate(
                    R.id.action_main_page_to_tech_inspection,
                    bundleOf(TechInspectionFragment.CAR_ID to this)
                )
            }
            status.onFail {
                error = it
                timber("updateSrts: $it")
            }
        }
        viewModel.fuel.observe(viewLifecycleOwner) { status ->
            status.onSuccess {
                if (this != null && this.marks != null && this.marks.size >= 3) {
                    Methods.saveModelLocally(FUEL_OBJECT, this)
                    fuelsAdapter = FuelAdapter(childFragmentManager, this)
                    vp_fuel.adapter = fuelsAdapter
                    Methods.showError("fuel", true)
                }
            }
            status.onFail {
                val city = Methods.getLocallySavedModel(FUEL_OBJECT, City::class.java) as City?
                if (city != null && city.marks.size >= 3) {
                    fuelsAdapter = FuelAdapter(childFragmentManager, city)
                    vp_fuel.adapter = fuelsAdapter
                    Methods.showError("fuel", true)
                } else {
                    if (!it.contains("подключение к сети") && Methods.showError("fuel")) {
                        toast("Не удалось загрузить данные о курсе бензина")
                        Methods.showError("fuel", false)
                    }
                    timber("fuel: $it")
                }
            }
        }
        viewModel.weather.observe(viewLifecycleOwner) { status ->
            status.onSuccess {
                Methods.saveModelLocally(WEATHER_OBJECT, this)
                showDividerLine(divider, true)
            }
            status.onFail {
                val weather =
                    Methods.getLocallySavedModel(WEATHER_OBJECT, Weather::class.java) as Weather?
                if (weather != null) {
                    divider.visibility = View.VISIBLE
                    setWeatherIcon(iv_weather, weather.icon)
                    setWeatherTemp(tv_weather, weather.temp)
                    showDividerLine(divider, true)
                } else {
                    layout_weather.visibility = View.INVISIBLE
                    showDividerLine(divider, false)
                }
            }
        }
        observeCurrencies()
//        viewModel.avgPrice.observe(viewLifecycleOwner) { status ->
//            status.onSuccess {
//                if (size == 0) return@onSuccess
//                val p = this[0]
//                viewModel.garageCars.value?.item?.firstOrNull { car -> car.vin == p.vin }?.let { car ->
//                    val price = p.price
//                    tv_auto_price_avg.text = if (price != null) "Средняя цена вашего ${car.title} - $price ₸" else
//                        "Не найдена информация по авто ${car.title}"
//                }
//            }
//            status.onFail {
//                if (!it.contains("подключение к сети")) toast(it)
//                timber("avgPrice: $it")
//            }
//        }
    }

    private val refreshIndicators =
        hashMapOf(
            MainPageItem.INSURANCE to false,
            MainPageItem.PENALTIES to false,
            MainPageItem.INSPECTION to false
        )

    private fun startRefreshAnimation(view: View, indicator: MainPageItem) {
        view.animate().rotationBy(30f).setDuration(60L).withEndAction {
            if (refreshIndicators[indicator] == true) startRefreshAnimation(view, indicator)
            else view.rotation = 0f
        }.start()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }
        fusedLocation.lastLocation.addOnCompleteListener {
            val location = it.result
            if (location != null) {
                Methods.saveLocation(location.latitude, location.longitude)
                viewModel.setLocation(location.latitude, location.longitude)
                Methods.showError("latlon", true)
            } else {
                val latlon = Methods.getLocation()
                if (latlon != null) {
                    viewModel.setLocation(latlon.first, latlon.second)
                    Methods.showError("latlon", true)
                } else if (Methods.showError("latlon")) {
                    toast(getString(R.string.location_not_found))
                    Methods.showError("latlon", false)
                }
            }
        }
    }

    private fun observeCurrencies() {
        viewModel.rub.observe(viewLifecycleOwner) { Methods.saveModelLocally(CURRENCY_RUB, it) }
        viewModel.usd.observe(viewLifecycleOwner) { Methods.saveModelLocally(CURRENCY_USD, it) }
        viewModel.eur.observe(viewLifecycleOwner) { Methods.saveModelLocally(CURRENCY_EUR, it) }
    }

    private fun getLocalCurrencies() {
        val currencyRub =
            Methods.getLocallySavedModel(CURRENCY_RUB, Currency::class.java) as Currency?
        val currencyUsd =
            Methods.getLocallySavedModel(CURRENCY_USD, Currency::class.java) as Currency?
        val currencyEur =
            Methods.getLocallySavedModel(CURRENCY_EUR, Currency::class.java) as Currency?

        if (currencyRub != null) {
            setCurrencyName(lbl_rub, currencyRub.title)
            setCurrencyValue(tv_rub, currencyRub.description)
            setCurrencyArrow(iv_rub, currencyRub.index)
        } else {
            layout_rub.visibility = View.INVISIBLE
        }
        if (currencyUsd != null) {
            setCurrencyName(lbl_usd, currencyUsd.title)
            setCurrencyValue(tv_usd, currencyUsd.description)
            setCurrencyArrow(iv_usd, currencyUsd.index)
        } else {
            layout_usd.visibility = View.INVISIBLE
        }
        if (currencyEur != null) {
            setCurrencyName(lbl_eur, currencyEur.title)
            setCurrencyValue(tv_eur, currencyEur.description)
            setCurrencyArrow(iv_eur, currencyEur.index)
        } else {
            layout_eur.visibility = View.INVISIBLE
        }
    }
}

private const val WEATHER_OBJECT = "WEATHER_OBJECT"
private const val CURRENCY_RUB = "CURRENCY_RUB"
private const val CURRENCY_EUR = "CURRENCY_EUR"
private const val CURRENCY_USD = "CURRENCY_USD"
private const val FUEL_OBJECT = "FUEL_OBJECT"