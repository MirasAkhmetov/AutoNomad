package com.autonomad.ui

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.*
import com.autonomad.data.models.FcmId
import com.autonomad.data.repo.CapsRepo
import com.autonomad.utils.SingleLiveEvent
import com.autonomad.utils.Status
import com.autonomad.utils.timber
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class NavigationViewModel : ViewModel(), KoinComponent {

    private val repo: CapsRepo by inject()

    private val mMenu = SingleLiveEvent(BottomNavMenu.MainMenu)
    val menu: LiveData<BottomNavMenu> = mMenu

    private val mIsConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = mIsConnected

    private var notificationIntent = mutableMapOf<String, String>()

    fun setMenu(newMenu: BottomNavMenu) {
        if (mMenu.value != newMenu) mMenu.value = newMenu
    }

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            timber("available")
            mIsConnected.postValue(true)
        }

        override fun onLost(network: Network) {
            timber("onLost")
            mIsConnected.postValue(false)
        }
    }

    fun putNotificationExtra(key: String, value: String) {
        notificationIntent[key] = value
    }

    fun getNotificationExtra(key: String) = notificationIntent.remove(key)

    fun onLogIn() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            viewModelScope.launch(Dispatchers.IO) {
                var result: Status<FcmId>? = null
                while (result?.isSuccess != true) {
                    result = repo.onLogIn(it.token)
                    delay(2000)
                }
            }
        }
    }

    fun onLogout() = liveData(Dispatchers.IO) {
        FirebaseInstanceId.getInstance().deleteInstanceId()
        emit("")
    }
}