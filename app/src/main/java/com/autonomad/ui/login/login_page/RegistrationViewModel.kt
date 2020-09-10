package com.autonomad.ui.login.login_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.login.Phone
import com.autonomad.data.models.login.PhoneWithCode
import com.autonomad.data.models.login.Registration
import com.autonomad.utils.*

class RegistrationViewModel : DisposableViewModel() {
    private val apiService = ApiCapsSingle.apiService

    val isSmsSent = MutableLiveData<Boolean>()

    private val mToken = MutableLiveData<Status<String>>()
    val token: LiveData<Status<String>> = mToken

    private var phoneNumber = ""

    fun register(name: String, phoneNumber: String, password: String) {
        mToken.value = Status.loading
        apiService.register(Registration(phoneNumber, name, password)).subscribeAndDispose({
            when {
                it.isSuccessful -> {
                    this.phoneNumber = phoneNumber
                    sendSMS()
                }
                it.errorBody()?.string()?.contains("exists") == true -> {
                    mToken.value = "Пользователь с таким номером уже зарегистрирован".fail
                }
                else -> it.message().fail
            }
        }, mToken::fromThrowable)
    }

    fun sendSMS() {
        mToken.value = Status.loading
        apiService.sendSms(Phone(phoneNumber)).subscribeAndDispose({
            isSmsSent.value = it.isSuccessful
            mToken.value = if (!it.isSuccessful) it.message().fail else Status.idle
        }, mToken::fromThrowable)
    }

    fun getToken(phoneWithCode: PhoneWithCode) {
        mToken.value = Status.loading
        apiService.verify(phoneWithCode).subscribeAndDispose({
            mToken.value = if (it.isSuccessful) it.body()?.token.success else it.message().fail
        }, mToken::fromThrowable)
    }
}
