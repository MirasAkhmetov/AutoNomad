package com.autonomad.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autonomad.R
import com.autonomad.data.models.SpecialOffer

class PerksPage :Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_profile_perks,container,false)

        val offers = ArrayList<SpecialOffer>()
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))
        offers.add(SpecialOffer("Kolesiki Group", "25% скидка на первую покупку"))

        val offer_rv = view.findViewById<RecyclerView>(R.id.rv_special_offer)
        offer_rv.setLayoutManager(LinearLayoutManager(context))
        offer_rv.setHasFixedSize(true)
        val offer_adapter = SpecialOfferAdapter(offers)
        offer_rv.setAdapter(offer_adapter)

        return view
    }
}