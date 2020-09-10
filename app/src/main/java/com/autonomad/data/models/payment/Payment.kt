package com.autonomad.data.models.payment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Credentials(
    @SerializedName("amount")
    @Expose
    val amount: String,
    @SerializedName("cp_public_id")
    @Expose
    val cp_public_id: String,
    @SerializedName("currency")
    @Expose
    val currency: String
)

data class Checkout(
    @SerializedName("cryptogram")
    @Expose
    val cryptogram: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("return_url")
    @Expose
    val return_url: String
)

data class PaymentResult(
    @SerializedName("message")
    @Expose
    val message: Message,
    @SerializedName("status")
    @Expose
    val status: String

)

data class Message(
    @SerializedName("transaction_id")
    @Expose
    val transaction_id: String,
    @SerializedName("pa_req")
    @Expose
    val pa_req: String,
    @SerializedName("acs_url")
    @Expose
    val acs_url: String,
    @SerializedName("term_url")
    @Expose
    val name: String
)

data class ThreeDSFinish(
    @SerializedName("PaRes")
    @Expose
    val PaRes: String,
    @SerializedName("MD")
    @Expose
    val MD: String
)

data class BankingCard(
    @SerializedName("count")
    @Expose
    val count: String,
    @SerializedName("results")
    @Expose
    val result: List<BankingCardResult>
)

data class BankingCardResult(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("card_first_six")
    @Expose
    val card_first_six: String,
    @SerializedName("card_last_four")
    @Expose
    val card_last_four: String,
    @SerializedName("card_exp_date")
    @Expose
    val card_exp_date: String,
    @SerializedName("card_type")
    @Expose
    val card_type: String
)
data class CardID(
    @SerializedName("card_id")
    @Expose
    var card_id: String

)
