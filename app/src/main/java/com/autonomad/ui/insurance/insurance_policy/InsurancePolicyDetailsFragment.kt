package com.autonomad.ui.insurance.insurance_policy

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.autonomad.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_insurance_policy_details.*
import java.lang.String.format


class InsurancePolicyDetailsFragment : Fragment(R.layout.fragment_insurance_policy_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancel.setOnClickListener {
            activity?.onBackPressed()
        }

        kasko_text_1.text =
            getSpannedText(format(requireContext().resources.getString(R.string.kasko_text_1)))
        kasko_text_2.text =
            getSpannedText(format(requireContext().resources.getString(R.string.kasko_text_2)))
        kasko_text_3.text =
            getSpannedText(format(requireContext().resources.getString(R.string.kasko_text_3)))

        arguments?.let {
            val type = arguments?.getInt("type", -1)
            when (type) {
                0 -> {
                    title.text = "Обязательное автострахование"
                    ogpo_text.visibility = VISIBLE
                    kasko_texts.visibility = GONE
                    mst_texts.visibility = GONE
                    request.setOnClickListener {
                        val url = "https://wa.me/77002101234?text=Хочу+купить+ОГПО"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
                    Picasso.get().load(R.drawable.image_ogpo).fit().into(image)
                }
                1 -> {
                    title.text = "КАСКО"
                    ogpo_text.visibility = GONE
                    kasko_texts.visibility = VISIBLE
                    mst_texts.visibility = GONE
                    request.setOnClickListener {
                        val url = "https://wa.me/77002101234?text=Хочу+купить+КАСКО"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
                    Picasso.get().load(R.drawable.image_kasko).fit().into(image)
                }
                2 -> {
                    title.text = "МСТ"
                    ogpo_text.visibility = GONE
                    kasko_texts.visibility = GONE
                    mst_texts.visibility = VISIBLE
                    request.setOnClickListener {
                        val url = "https://wa.me/77002101234?text=Хочу+купить+МСТ"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
                    Picasso.get().load(R.drawable.image_mst).fit().into(image)
                }
            }
        }
    }

    private fun getSpannedText(text: String): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(text)
        }
    }

}