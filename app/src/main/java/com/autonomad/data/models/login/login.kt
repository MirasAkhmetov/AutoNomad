package com.autonomad.data.models.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("username")
    @Expose
    val username: String,
    @SerializedName("password")
    @Expose
    val password: String
)

data class Token(val token: String)

data class Registration(
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("first_name")
    @Expose
    val first_name: String,
    @SerializedName("password")
    @Expose
    val password: String
)

data class UserInfo(
    @SerializedName("phone")
    @Expose
    val phone: String?,
    @SerializedName("email")
    @Expose
    val email: String?,
    @SerializedName("first_name")
    @Expose
    val first_name: String?,
    @SerializedName("last_name")
    @Expose
    val last_name: String?,
    @SerializedName("patronymic")
    @Expose
    val patronymic: String?,
    @SerializedName("avatar")
    @Expose
    val avatar: String?
)

data class Phone(
    @SerializedName("phone")
    @Expose
    val phone: String
)

data class PhoneWithCode(
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("code")
    @Expose
    val code: Int
)

data class Message(val message: String)

data class Password(val password: String)

