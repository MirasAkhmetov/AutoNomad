package com.autonomad.ui.claims.specialist_claims.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autonomad.R
import com.autonomad.data.models.claims.ServiceOffer
import com.autonomad.ui.bottom_sheet.RoundBottomDialog
import kotlinx.android.synthetic.main.bottom_sheet_claim_specialist_add_service.*

class EditServiceDialog(
    private val service: ServiceOffer,
    private val onSave: (String) -> Unit,
    private val onDelete: () -> Unit
) :
    RoundBottomDialog() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.bottom_sheet_claim_specialist_add_service, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_service_name.text = service.subcategory.name
        et_price.setText(service.price?.toString().orEmpty())
        btn_delete.setOnClickListener {
            onDelete()
            dismiss()
        }
        btn_save.setOnClickListener {
            if (et_price.text.toString() != service.price?.toString().orEmpty()) {
                onSave(et_price.text.toString())
            }
            dismiss()
        }
        iv_back.setOnClickListener { dismiss() }
    }
}