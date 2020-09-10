package com.autonomad.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.autonomad.NotificationsService
import com.autonomad.R
import com.autonomad.R.id.*
import com.autonomad.data.models.pref.repo.LocalStorage
import com.autonomad.ui.check_auto.report.ThreeDS
import com.autonomad.utils.HttpClient
import com.autonomad.utils.Methods
import com.autonomad.utils.timber
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.inject
import ru.cloudpayments.sdk.three_ds.ThreeDSDialogListener

class MainActivity : AppCompatActivity(), ThreeDSDialogListener, LoginListener {

    private val viewModel by viewModels<NavigationViewModel>()
    private val localStorage: LocalStorage by inject()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        HttpClient.provideContext(this)
        Methods.setSharedPref(this)
        setContentView(R.layout.activity_main)
        bottom_nav = findViewById(nav_view)
        navController = findNavController(nav_host_fragment)
        bottom_nav.setupWithNavController(navController)
        visibilityNavElements(navController)
        registerCallback()
        observe()

        if (intent?.hasExtra(NotificationsService.PUSH_NOTIFICATION_TYPE) == true) {
            intent.extras?.keySet()?.forEach { k -> viewModel.putNotificationExtra(k, intent.extras?.get(k).toString()) }
        }
    }

    override fun onResume() {
        setLoginListener(this)
        val current = System.currentTimeMillis()
        val lastActive = Methods.getLastActive()
        if (lastActive == -1L || current - lastActive > (MAX_PASSIVE_MINUTES * 60 * 1000)) checkToken()
        super.onResume()
    }

    override fun onPause() {
        setLoginListener(null)
        Methods.updateActive()
        super.onPause()
    }

    override fun onDestroy() {
        unregisterCallback()
        super.onDestroy()
    }

    private fun checkToken() {
        if (Methods.key.isNotEmpty()) {
            timber(Methods.key)
            if (localStorage.isPinLoginEnabled()) navController.navigate(pinCodePage)
        }
    }

    private fun observe() {
        viewModel.menu.observe(this) {
            setMenuWithController(it.menu, it.destinations)
        }
    }

    private fun setStartMain(isMain: Boolean) {
        val navGraph = navController.graph
        timber("currentMainGraph: ${navGraph.startDestination}, mainPage: $main_page, loginPage: $loginFragment")
        if ((navGraph.startDestination == main_page && isMain) || (navGraph.startDestination == loginFragment && !isMain)) return
        navGraph.startDestination = if (isMain) main_page else loginFragment
        navController.graph = navGraph
    }

    private var lastDestination = 0

    @SuppressLint("RestrictedApi")
    private fun visibilityNavElements(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, args ->
            lastDestination = destination.id
            when (destination.id) {
                storiesFragment, loginFragment, passwordRecoveryStep1, passwordRecoveryStep3,
                userPageFragment, expensesFragment, penaltyTicketFragment, penaltiesOfPerson,
                pinCodePage, calendarFragment, ticketsFragment, detailTicketInformation, parkingCalendar, messages, penaltyDetail,
                makeOrder, settingsPage, bankingCardAdd, newDocument, checkAutoInfo, detailTicket, report, detailMaster,
                feedback2, createClaim, replies, favourites, rate, repairAuto, detailClaim, payment, categoryList,
                detailCategPage1, createClaim1, createClaim2, createClaimMain2, createClaim3, createClaim4, detailClaim2, specialist_profile,
                specialistCategoryList, specialist_add_offer, claim_specialist_contacts, specialist_add_offer_image, insurance_page,
                tech_inspection_fragment, claim_specialist_tariffs, fragment_settings_addresses_edit, carsPageDetail, bankSpisok, documentSett, changePass,
                insurance_policy_details, driversPageDetail -> {
                    hideNav()
                    supportActionBar?.setShowHideAnimationEnabled(false)
                    supportActionBar?.hide()
                }
                claim_specialist_support, claim_specialist_statistics, main_page, services, fragment_settings_addresses,
                shop_home, shop_basket, shop_settings, shop_fav_products, shop_orders, shop_order_details, shop_product,
                shop_products_by_category, shop_category -> {
                    showNav()
                    supportActionBar?.setShowHideAnimationEnabled(false)
                    supportActionBar?.hide()
                }
                else -> {
                    showNav()
                    supportActionBar?.setShowHideAnimationEnabled(false)
                    supportActionBar?.show()
                }
            }
            if (destination.id in listOf(service_exit, main_page, services, profile, service_exit)) {
                viewModel.setMenu(BottomNavMenu.MainMenu)
                if (destination.id == service_exit) navController.navigate(action_global_services)
                else if (bottom_nav.selectedItemId != destination.id) bottom_nav.selectedItemId = destination.id
            } else if (destination.id == pinCodePage) Methods.stopTime()
            when (args?.getString(MESSAGE)) {
                SET_MAIN -> setStartMain(true)
                SET_LOGIN -> setStartMain(false)
                PIN_ENTERED -> Methods.pinEntered()
            }
        }
    }

    private fun registerCallback() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            val network = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            connectivityManager.registerNetworkCallback(network, viewModel.networkCallback)
        } else
            connectivityManager.registerDefaultNetworkCallback(viewModel.networkCallback)
    }

    private fun unregisterCallback() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(viewModel.networkCallback)
    }

    private fun setMenuWithController(@MenuRes menu: Int, destinations: Set<Int>) {
        timber(
            when (menu) {
                R.menu.main_bottom_nav_menu -> "MainBottomMenu"
                R.menu.parking_bottom_nav_menu -> "ParkingBottomMenu"
                R.menu.insurance_bottom_nav -> "InsuranceBottomMenu"
                R.menu.check_auto_bottom_nav_menu -> "CheckAutoBottomMenu"
                R.menu.claim_specialist_bottom_nav_menu -> "ClaimSpecBottomMenu"
                R.menu.claim_user_bottom_nav_menu -> "ClaimUserBottomMenu"
                R.menu.penalty_bottom_nav_menu -> "PenaltyBottomMenu"
                else -> "UnknownBottomMenu"
            }
        )
        if (bottom_nav.menu.getItem(0).itemId in destinations) return
        timber("${bottom_nav.menu.getItem(0).itemId} -> $destinations")
        bottom_nav.menu.clear()
        bottom_nav.inflateMenu(menu)
        setupActionBarWithNavController(navController, AppBarConfiguration(destinations))
    }

    companion object {
        const val SET_MAIN = "setMain"
        const val SET_LOGIN = "setLogin"
        const val MESSAGE = "navigationMessage"
        const val PIN_ENTERED = "pinEntered"

        private const val MAX_PASSIVE_MINUTES = 5

        private lateinit var myInterface: ThreeDS

        fun setListener(myInterface: ThreeDS) {
            this.myInterface = myInterface
        }

        private lateinit var bottom_nav: BottomNavigationView

        fun hideNav() {
            if (bottom_nav.isVisible) bottom_nav.isVisible = false
        }

        fun showNav() {
            if (!bottom_nav.isVisible) bottom_nav.isVisible = true
        }

        private var loginListener: LoginListener? = null

        fun setLoginListener(listener: LoginListener?) {
            loginListener = listener
        }

        fun onLogin() {
            loginListener?.onLogin()
        }

        fun onLogout() = loginListener?.onLogout()
    }

    override fun onAuthorizationCompleted(md: String?, paRes: String?) {
        myInterface.success(md, paRes)
    }

    override fun onAuthorizationFailed(html: String?) {
        myInterface.error()
    }

    override fun onLogin() {
        viewModel.onLogIn()
    }

    override fun onLogout() = viewModel.onLogout().apply {
        observe(this@MainActivity) {
            if (it.isEmpty()) {
                localStorage.clear()
                removeObservers(this@MainActivity)
            }
        }
    }
}
