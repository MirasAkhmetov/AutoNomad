package com.autonomad.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.autonomad.R
import com.autonomad.data.models.claims.PluralForms
import com.autonomad.data.models.claims.getPlural
import com.autonomad.data.models.main_page_car.Items
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "BindingAdaptersLogcat"

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["oldDateString", "newPattern", "pattern", "prefix"], requireAll = false)
fun formatDate(view: TextView, oldDateString: String?, newPattern: String?, pattern: String?, prefix: String?) {
    if (oldDateString == null || newPattern == null) {
        view.text = ""
        return
    }
    val format = SimpleDateFormat(pattern ?: "yyyy-MM-dd", Locale.US)
    try {
        format.parse(oldDateString)?.let { parsed ->
            val newFormat = SimpleDateFormat(newPattern, Locale.US).apply {
                dateFormatSymbols = DateFormatSymbols().also { it.months = months }
            }
            view.text = (prefix.orEmpty()) + " ${newFormat.format(parsed)}"
        }
    } catch (e: ParseException) {
        Timber.tag(TAG).e(e, "formatDate:")
    }
}

@BindingAdapter("stateNumber", "part")
fun setStateNumber(textView: TextView, stateNumber: String?, part: Int) {
    if (stateNumber == null || stateNumber.isEmpty()) {
        textView.text = ""
        return
    }
    textView.text = when {
        isNewFormat(stateNumber) -> {
            when (part) {
                0 -> stateNumber.substring(0, 3)
                1 -> stateNumber.substring(3, 6)
                2 -> stateNumber.substring(6)
                else -> ""
            }
        }
        isOldFormat(stateNumber) -> {
            when (part) {
                0 -> stateNumber.substring(0, 1)
                1 -> stateNumber.substring(1, 4)
                2 -> stateNumber.substring(4)
                else -> ""
            }
        }
        else -> ""
    }
}

@BindingAdapter(value = ["imageUrl", "error", "placeholder"], requireAll = false)
fun setImageUrl(imageView: ImageView, url: String?, error: Drawable?, placeholder: Drawable?) {
    Picasso.get().load(url).apply {
        if (error != null) error(error)
        if (placeholder != null) placeholder(placeholder)
        into(imageView)
    }
}

@BindingAdapter("imageRes")
fun setImageRes(imageView: ImageView, drawableRes: Drawable) {
    imageView.setImageDrawable(drawableRes)
}

@BindingAdapter("weatherTemperature")
fun setWeatherTemp(textView: TextView, temp: String?) {
    temp?.let {
        textView.text = it.plus("°C")
    }
}

@BindingAdapter("dividerLine")
fun showDividerLine(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("currencyName")
fun setCurrencyName(textView: TextView, currencyName: String?) {
    currencyName?.let { textView.text = it }
}

@BindingAdapter("networkValue")
fun setNetworkValueVisibility(textView: TextView, isConnected: Boolean) {
    textView.visibility = if (isConnected) View.GONE else View.VISIBLE
}

@BindingAdapter("currencyValue")
fun setCurrencyValue(textView: TextView, currencyValue: String?) {
    currencyValue?.let { textView.text = it }
}

@BindingAdapter("currencyArrow")
fun setCurrencyArrow(imageView: ImageView, index: String?) {
    imageView.rotation = if (index == "UP") 180F else 0F
    imageView.visibility = if (index == null) View.GONE else View.VISIBLE
    imageView.setColorFilter(
        ContextCompat.getColor(
            imageView.context,
            if (index == "UP") R.color.situational_error else R.color.Red
        )
    )
}

@BindingAdapter("weatherIcon")
fun setWeatherIcon(imageView: ImageView, icon: Items?) {
    imageView.setImageResource(
        when (icon?.name) {
            "01d" -> R.drawable.ic_01d
            "01n" -> R.drawable.ic_01n
            "02d" -> R.drawable.ic_02d
            "02n" -> R.drawable.ic_02n
            "03d", "03n" -> R.drawable.ic_03d
            "04d", "04n" -> R.drawable.ic_04d
            "09d", "09n" -> R.drawable.ic_09d
            "10d" -> R.drawable.ic_10d
            "10n" -> R.drawable.ic_10n
            "11d", "11n" -> R.drawable.ic_11d
            "13d", "13n" -> R.drawable.ic_13d
            "50d", "50n" -> R.drawable.ic_50d
            else -> return
        }
    )
}

@BindingAdapter("cardSrc")
fun setCardImage(imageView: ImageView, cardSrc: String?) {
    if (cardSrc == null) return
    imageView.setImageResource(
        when {
            cardSrc.startsWith("4") -> R.drawable.ic_card_visa
            cardSrc.startsWith("5") -> R.drawable.ic_card_mc
            else -> return
        }
    )
}

@BindingAdapter("mastersCount")
fun setMastersCount(textView: TextView, count: Int?) {
    if (count == null || count < 1) return
    val masters = PluralForms("специалист", "специалиста", "специалистов")
    textView.text = getPlural(count, masters, false)
}