package com.autonomad.ui.login.login_page

import androidx.lifecycle.LiveData
import com.autonomad.data.models.login.Auth
import com.autonomad.data.models.login.Token
import com.autonomad.utils.*

class LoginPageViewModel : DisposableViewModel() {
    private val apiService = ApiCapsSingle.apiService

    private val mToken = SingleLiveEvent<Status<Token>>()
    val token: LiveData<Status<Token>> = mToken

    fun check(phoneNumber: String, password: String) {
        mToken.value = Status.loading
        apiService.login(Auth(phoneNumber, password)).subscribeAndDispose({
            mToken.value = when {
                it.isSuccessful -> it.toStatus()
                it.code() == 400 -> Status.idle
                it.errorBody()?.string()?.contains("activate", true) == true || it.code() == 412 -> "412$phoneNumber".fail
                else -> it.message().fail
            }
        }, mToken::fromThrowable)
    }
}
