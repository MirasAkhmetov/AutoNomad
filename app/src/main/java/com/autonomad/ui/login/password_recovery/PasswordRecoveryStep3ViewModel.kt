package com.autonomad.ui.login.password_recovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.login.Password
import com.autonomad.utils.*

class PasswordRecoveryStep3ViewModel : DisposableViewModel() {
    private val apiService = ApiCapsSingle.apiService
    var success = MutableLiveData<Boolean>()

    private val mChanged = MutableLiveData<Status<Boolean>>()
    val changed: LiveData<Status<Boolean>> = mChanged

    fun changePassword(password: String, token: String) {
        mChanged.value = Status.loading
        apiService.changePassword(Password(password), token).subscribeAndDispose({
            mChanged.value = when {
                it.isSuccessful -> true.success
                it.code() == 403 -> "Вы не можете поменять пароль в данный момент".fail
                else -> it.message().fail
            }
        }, mChanged::fromThrowable)
    }
}