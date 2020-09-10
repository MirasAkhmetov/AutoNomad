package com.autonomad.ui.profile

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autonomad.data.models.login.UserUpdate
import com.autonomad.utils.ApiCapsFactory
import com.autonomad.utils.BaseViewModel
import com.autonomad.utils.Methods
import com.autonomad.utils.timber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class UserPageViewModel : BaseViewModel() {

    var booleeaan = MutableLiveData<Boolean>()

    private var bitmap: Bitmap? = null
    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        upload(bitmap)
    }

    fun patchProfile(
        token: String,
        email: String,
        firstName: String,
        lastName: String,
        patronymic: String,
        gender: Int,
        dateOfBirth: String
    ) {
        dataLoading.value = true
        val d = ApiCapsFactory.instance.apiService
            .updateUser(
                token, UserUpdate(
                    if (email.isNotEmpty()) email else null,
                    if (firstName.isNotEmpty()) firstName else null,
                    if (lastName.isNotEmpty()) lastName else null,
                    if (patronymic.isNotEmpty()) patronymic else null,
                    if (gender > 0) gender else null,
                    if (dateOfBirth.isNotEmpty()) dateOfBirth else null
                )
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                dataLoading.value = false
                println(data.code())
                when (data.code()) {
                    200, 201 -> {
                        Log.d("profka", data.body().toString())
                        booleeaan.value = true
                    }
                    404 -> {
                        booleeaan.value = false
                        Log.d("profka2", data.body().toString())
                    }
                    500 -> {
                        booleeaan.value = false
                        Log.d("profka5", data.body().toString())
                    }
                    else -> {
                        booleeaan.value = false
                        Log.d("profka6", data.body().toString())
                    }
                }
            }, { throwable ->
                Log.d("Tagi", throwable.toString())
                dataLoading.value = false
                empty.value = false
                booleeaan.value = false
            })
        disposable.add(d)
    }

    private fun upload(bitmap: Bitmap) {
        val init = System.currentTimeMillis()
        val scaledBitmap = Bitmap.createScaledBitmap(cropBitmap(bitmap), 512, 512, false)
        val bos = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos)
        val bytes = bos.toByteArray()
        val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
        val photo =
            MultipartBody.Part.createFormData(
                "avatar",
                "$init${System.currentTimeMillis() / 1000}.jpg",
                requestFile
            )

        val d = ApiCapsFactory.instance.apiService.uploadAvatar(Methods.getToken(), photo)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                timber(it)
                when (it.code()) {
                    200, 201 -> {
                        booleeaan.value = true
                    }
                    else -> booleeaan.value = false
                }
            }
        disposable.add(d)
    }

    private fun cropBitmap(src: Bitmap) = if (src.height > src.width) {
        Bitmap.createBitmap(src, 0, (src.height - src.width) / 2, src.width, src.width)
    } else {
        Bitmap.createBitmap(src, (src.width - src.height) / 2, 0, src.height, src.height)
    }

}