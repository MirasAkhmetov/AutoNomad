package com.autonomad.utils

import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.Page
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.util.*

class Status<out T> private constructor(
    val message: String? = null,
    val item: T? = null,
    private val state: State
) {


    fun onFailure(call: String.() -> Unit) {
        if (state == State.FAILED) message?.call()
    }

    fun onFail(call: (String) -> Unit) {
        if (state == State.FAILED && message != null) call(message)
    }

    fun onSuccess(call: T.() -> Unit) {
        if (state == State.SUCCESS) item?.call()
    }

    override fun toString() = "Status(item = $item, message = $message, state = $state)"

    val isSuccess: Boolean = state == State.SUCCESS && item != null
    val isFailure: Boolean = state == State.FAILED && message != null
    val isLoading: Boolean get() = state == State.LOADING
    val isIdle: Boolean get() = state == State.IDLE

    companion object {
        fun <T> success(value: T?) = if (value != null) Status<T>(item = value, state = State.SUCCESS) else null

        fun <T> successful(value: T) = Status(item = value, state = State.SUCCESS)

        fun fail(error: String?): Status<Nothing> = if (error != null) {
            timber("fail: $error")
            Status(message = error, state = State.FAILED)
        } else Status(message = "", state = State.FAILED)

        fun failed(error: String): Status<Nothing> {
            timber("fail: $error")
            return Status(message = error, state = State.FAILED)
        }

        fun <T> setLoaded(liveData: MutableLiveData<Status<T>>) {
            val item = liveData.value?.item
            liveData.postValue(Status(item = item, state = State.LOADING))
        }

        fun <T> setFailed(liveData: MutableLiveData<Status<T>>, message: String?) {
            val item = liveData.value?.item
            liveData.postValue(Status(message ?: "", item, State.FAILED))
        }

        val loading = Status<Nothing>(state = State.LOADING)

        val idle = Status<Nothing>(state = State.IDLE)
    }
}

val <T> T?.success get() = Status.success(this)

val <T> T.successful get() = Status.successful(this)

val String?.fail: Status<Nothing>?
    get() = Status.fail(
        when {
            this?.contains("resolve host") == true || this?.contains("auto-nomad.kz") == true -> "Не удалось загрузить данные. Проверьте подключение к сети"
            this?.toLowerCase(Locale.ROOT)?.contains("timeout") == true -> "Превышено время ожидания"
            else -> this
        }
    )

val String.failed: Status<Nothing>
    get() = Status.failed(
        when {
            contains("resolve host") || contains("auto-nomad.kz") -> "Не удалось загрузить данные. Проверьте подключение к сети"
            toLowerCase(Locale.ROOT).contains("timeout") -> "Превышено время ожидания"
            else -> this
        }
    )

inline fun <reified T> Response<T>.toStatus() = if (isSuccessful) body().success else {
    Timber.tag(T::class.java.name).e(message())
    (message() ?: errorBody()?.string()).fail
}

fun <T> Response<T>.convert() = if (isSuccessful) body().success else {
    val errorBody = JSONObject(errorBody()?.string().orEmpty())
    timber("requestFailMessage: ${message()}")
    timber("requestFailErrorBody: $errorBody")
    if (errorBody.has("message")) errorBody.get("message").toString().fail
    message().fail
}

inline fun <reified T> Response<Page<T>>.listToStatus() = if (isSuccessful) body()?.list.success else {
    Timber.tag(T::class.java.name).e(message())
    (message() ?: errorBody()?.string()).fail
}

inline fun <reified T> MutableLiveData<Status<T>>.fromResponse(response: Response<T>) {
    value = response.toStatus()
}

inline fun <reified T> MutableLiveData<Status<T>>.fromThrowable(t: Throwable) {
    Timber.tag(T::class.java.name).e(t.toString())
    value = t.localizedMessage.fail
}

fun <T> Status<Response<T>>.unwrap() = when {
    this.isSuccess -> item?.body()?.success
    this.isFailure -> message.fail
    this.isIdle -> Status.idle
    else -> Status.loading
}

private enum class State { SUCCESS, FAILED, LOADING, IDLE }