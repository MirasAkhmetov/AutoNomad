package com.autonomad.ui.login.password_recovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.login.Phone
import com.autonomad.data.models.login.PhoneWithCode
import com.autonomad.utils.*

class PasswordRecoveryStep1ViewModel : DisposableViewModel() {
    private val apiService = ApiCapsSingle.apiService

    private val mSmsSent = MutableLiveData<Status<Boolean>>()
    val smsSent: LiveData<Status<Boolean>> = mSmsSent

    private val mConfirm = SingleLiveEvent<Status<String>>()
    val confirm: LiveData<Status<String>> = mConfirm

    private var phoneNumber = ""

    fun sendSMS(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        mSmsSent.value = Status.loading
        apiService.forgotPasswordSendSms(Phone(phoneNumber)).subscribeAndDispose({
            val errorBody = it.errorBody()?.string()
            timber("errorBody: $errorBody")
            mSmsSent.value = if (it.isSuccessful) true.success else it.message().fail
        }, mSmsSent::fromThrowable)
    }

    fun enterCode(code: Int, isActivation: Boolean) {
        mConfirm.value = Status.loading
        val phoneWithCode = PhoneWithCode(phoneNumber, code)
        (if (isActivation) apiService.verify(phoneWithCode) else apiService.getToken(phoneWithCode)).subscribeAndDispose({
            mConfirm.setValue(if (it.isSuccessful) it.body()?.token.success else it.message().fail)
        }, mConfirm::fromThrowable)
    }
}