package com.autonomad.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.regex.Pattern

fun Fragment.navigateBack(@IdRes action: Int? = null) {
    onBackPress {
        if (action == null) findNavController().popBackStack()
        else {
            findNavController().navigate(action)
        }
    }
}

inline fun Fragment.onBackPress(crossinline action: () -> Unit) {
    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            action()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
}

fun Fragment.toast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Any.timber(message: String?) {
    Timber.tag("${javaClass.simpleName}Logcat").d(message)
}

fun Any.timber(message: Any?) {
    timber(message.toString())
}

fun Any.timberE(e: Exception, message: String) {
    Timber.tag("${javaClass.simpleName}Logcat").e(e, message)
}

fun Fragment.tt(message: String) {
    toast(message)
    timber(message)
}

fun Fragment.tt(toast: String, timber: String) {
    toast(toast)
    timber(timber)
}

fun ImageView.loadImage(url: String?, @DrawableRes error: Int? = null) {
    val picasso = Picasso.get().load(url)
    if (error != null) picasso.error(error)
    picasso.into(this)
}

inline fun ImageView.loadImageWithCallback(
    url: String,
    @DrawableRes error: Int? = null,
    crossinline onLoadSuccess: () -> Unit,
    crossinline onLoadFail: (String) -> Unit = {
        timber("loadImageWithCallback error: $it")
    }
) {
    timber("loadImageWithCallback url: $url")
    val picasso = Picasso.get().load(url)
    if (error != null) picasso.error(error)
    picasso.into(this, object : Callback {
        override fun onSuccess() {
            onLoadSuccess()
        }

        override fun onError(e: Exception?) {
            onLoadFail(e?.message.orEmpty())
        }
    })
}

fun isNewFormat(stateNumber: String?) = Pattern.compile("^[0-9]{3}[A-Z]{3}[0-9]{2}").matcher(stateNumber.orEmpty()).matches()

fun isOldFormat(stateNumber: String?) = Pattern.compile("^[A-Z][0-9]{3}[A-Z]{3}").matcher(stateNumber.orEmpty()).matches()

fun createRetrofit(baseUrl: String, client: OkHttpClient): Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .baseUrl(baseUrl)
    .client(client)
    .build()

fun dpToPx(value: Int, context: Context): Int {
    return (value * context.resources.displayMetrics.density).toInt()
}

typealias OnItemIdSelected = (Int) -> Unit
