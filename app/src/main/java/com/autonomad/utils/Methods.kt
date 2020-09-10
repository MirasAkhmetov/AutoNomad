package com.autonomad.utils

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.autonomad.data.models.meteo_currency.Weather
import com.autonomad.ui.MainActivity
import com.google.gson.Gson

object Methods {

    private lateinit var sharedPref: SharedPreferences

    private const val TOKEN = "token"
    private const val IS_SPECIALIST = "isSpecialist"
    private const val NAME = "name"
    private const val LATITUDE = "latitude"
    private const val LONGITUDE = "longitude"
    private const val WEATHER_OBJECT = "WEATHER_OBJECT"
    private const val LAST_ACTIVE = "last_active"

    var key: String
        get() = sharedPref.getString(TOKEN, "").orEmpty()
        set(value) {
            sharedPref.edit().putString(TOKEN, value).apply()
            MainActivity.onLogin()
        }

    var name: String?
        get() = sharedPref.getString(NAME, "")
        set(value) {
            sharedPref.edit().putString(NAME, value).apply()
        }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun setSharedPref(activity: AppCompatActivity) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
    }

    fun getToken(): String {
        return "Token " + sharedPref.getString(TOKEN, "")
    }

    fun deleteToken(lifecycleOwner: LifecycleOwner, onComplete: (String) -> Unit) {
        MainActivity.onLogout()?.observe(lifecycleOwner) {
            if (it.isEmpty()) sharedPref.edit().clear().apply()
            onComplete(it)
        } ?: onComplete("setListener failure(\"NullPointer\")")
    }

    fun isSpecialist(): Boolean {
        return sharedPref.getBoolean(IS_SPECIALIST, false)
    }

    fun setSpecialist(state: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(IS_SPECIALIST, state)
            commit()
        }
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        sharedPref.edit().putFloat(LATITUDE, latitude.toFloat()).putFloat(LONGITUDE, longitude.toFloat()).apply()
    }

    fun getLocation(): Pair<Double, Double>? {
        val latitude = sharedPref.getFloat(LATITUDE, 1000f).toDouble()
        val longitude = sharedPref.getFloat(LONGITUDE, 1000f).toDouble()
        return if (latitude != 1000.0 && longitude != 1000.0) latitude to longitude else null
    }

    private const val SUPPORT_FIRST_CARD_VISIBLE = "support_first_card_visibility"
    var supportFirstCardVisible: Boolean
        get() {
            return sharedPref.getBoolean(SUPPORT_FIRST_CARD_VISIBLE, true)
        }
        private set(value) = sharedPref.edit().putBoolean(SUPPORT_FIRST_CARD_VISIBLE, value).apply()

    fun swapFirstCardVisibility() {
        supportFirstCardVisible = !supportFirstCardVisible
    }

    private const val SUPPORT_SECOND_CARD_VISIBLE = "support_second_card_visibility"
    var supportSecondCardVisible: Boolean
        get() {
            return sharedPref.getBoolean(SUPPORT_SECOND_CARD_VISIBLE, true)
        }
        private set(value) = sharedPref.edit().putBoolean(SUPPORT_SECOND_CARD_VISIBLE, value).apply()

    fun swapSecondCardVisibility() {
        supportSecondCardVisible = !supportSecondCardVisible
    }

    fun showError(type: String, value: Boolean? = null): Boolean {
        if (value != null) {
            sharedPref.edit().putBoolean(type, value).apply()
            return value
        }
        return sharedPref.getBoolean(type, true)
    }

    fun saveWeather(weather: Weather) {
        sharedPref.edit().putString(WEATHER_OBJECT, Gson().toJson(weather)).apply()
    }

    fun getSavedWeather(): Weather? {
        return try {
            Gson().fromJson(sharedPref.getString(WEATHER_OBJECT, ""), Weather::class.java)
        } catch (ex: Exception) {
            null
        }
    }

    fun saveModelLocally(storeName: String, model: Any) {
        sharedPref.edit().putString(storeName, Gson().toJson(model)).apply()
    }

    fun getLocallySavedModel(storeName: String, model: Class<out Any>): Any? {
        return try {
            Gson().fromJson(sharedPref.getString(storeName, ""), model)
        } catch (ex: Exception) {
            null
        }
    }

    fun stopTime() {
        sharedPref.edit().remove(LAST_ACTIVE).apply()
    }

    fun getLastActive() = sharedPref.getLong(LAST_ACTIVE, -1L)

    fun updateActive(): Boolean {
        if (!sharedPref.contains(LAST_ACTIVE)) return false
        pinEntered()
        return true
    }

    fun pinEntered() {
        val time = System.currentTimeMillis()
        sharedPref.edit().putLong(LAST_ACTIVE, time).apply()
    }
}