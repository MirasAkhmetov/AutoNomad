package com.autonomad.data.models.claims

import com.google.gson.annotations.SerializedName

data class Tariff(
    val id: Int,
    val name: String,
    @SerializedName("rate_type")
    val rateType: Int,
    @SerializedName("response_count")
    val responseCount: Int?,
    @SerializedName("days_count")
    val daysCount: Int?,
    val price: String
) {
    val count: String
        get() {
            return when {
                responseCount != null && responseCount > 0 ->
                    getPlural(responseCount, PluralForms("отклик", "отклика", "откликов"), false)
                daysCount != null && daysCount > 0 ->
                    getPlural(daysCount, PluralForms("день", "дня", "дней"), false)
                else -> ""
            }
        }
}

data class TariffPost(
    @SerializedName("rate_fabric")
    val tariffId: Int,
    @SerializedName("promocode_uuid")
    val promo: String? = null
)

data class TariffCreated(
    val id: Int,
    @SerializedName("rate_fabric")
    val tariffId: Int,
    @SerializedName("promocode_uuid")
    val promo: String? = null
)

data class TariffOrder(
    val id: Int,
    @SerializedName("rate_fabric")
    val tariff: Tariff?,
    val rate: Int?,
    val price: String?,
    @SerializedName("order_id")
    val orderId: String?,
    @SerializedName("final_price")
    val finalPrice: String?,
    @SerializedName("purchased_at")
    val purchasedAt: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("promocode_uuid")
    val promo: String?,
    val status: Int?,
    @SerializedName("error_message")
    val error: String?
)