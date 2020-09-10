package com.autonomad.ui.claims.user_claims.claims.claimtwo

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.autonomad.data.models.claim_user.UplooodImage
import com.autonomad.utils.ApiMyRegFactory
import com.autonomad.utils.Methods
import com.autonomad.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.concurrent.atomic.AtomicInteger

class AddOfferImageViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "AddImageViewModelLogcat"

    private val disposable = CompositeDisposable()
    private val apiService = ApiMyRegFactory.apiService

    val uplodka = SingleLiveEvent<UplooodImage>()

    val mResult = SingleLiveEvent<Int>()
    val result: LiveData<Int> = mResult

    var uploads = AtomicInteger(0)
    var failures = AtomicInteger(0)

    fun upload(images: List<Bitmap>) {
        for (bitmap in images) {
            val init = System.currentTimeMillis()
            val scaledBitmap = Bitmap.createScaledBitmap(cropBitmap(bitmap), 512, 512, false)
            val bos = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos)
            val bytes = bos.toByteArray()
            val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
            val photo =
                MultipartBody.Part.createFormData("image", "$init${System.currentTimeMillis() / 1000}.jpg", requestFile)

            val d = apiService.uploadImage(Methods.getToken(), photo)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful){
                        uplodka.value = it.body()!!
                        uploads.incrementAndGet()
                    } else {
                        failures.incrementAndGet()
                    }
                    if (uploads.get() + failures.get() == images.size) {
                        mResult.value = getFailedCount(images.size)
                    }
                }, {
                    if (uploads.get() + failures.incrementAndGet() == images.size)
                        mResult.value = getFailedCount(images.size)
                })
            disposable.add(d)
        }
    }

    private fun getFailedCount(requestsCount: Int) = if (requestsCount == failures.get()) -1 else failures.get()

    private fun cropBitmap(src: Bitmap) = if (src.height > src.width) {
        Bitmap.createBitmap(src, 0, (src.height - src.width) / 2, src.width, src.width)
    } else {
        Bitmap.createBitmap(src, (src.width - src.height) / 2, 0, src.height, src.height)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}