package com.autonomad.ui.claims.specialist_claims.settings.add_offer_image

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.autonomad.R
import kotlinx.android.synthetic.main.fragment_claims_specialist_add_offer_image.*
import java.io.FileNotFoundException
import java.io.IOException

class AddOfferImageFragment : Fragment(R.layout.fragment_claims_specialist_add_offer_image) {
    private val viewModel by viewModels<AddOfferImageViewModel>()
    private val adapter = AddOfferImageAdapter(::onAddClick)

    companion object {
        private const val PICK_PHOTO_REQUEST_CODE = 7884
        private const val TAKE_PHOTO_REQUEST_CODE = 8466
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_images.adapter = adapter
        iv_back.setOnClickListener { activity?.onBackPressed() }
        tv_complete.setOnClickListener {
            adapter.getImages().also {
                if (it.isEmpty()) Toast.makeText(context, "Выберите фотографии", Toast.LENGTH_SHORT).show()
                else viewModel.upload(it)
            }
        }
        viewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    Toast.makeText(context, "Фотографии успешно загружены", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                }
                -1 -> { Toast.makeText(context, "Не удалось загрузить фотографии", Toast.LENGTH_SHORT).show() }
                else -> {
                    Toast.makeText(context, "Не удалось загрузить некоторые фотографии", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                TAKE_PHOTO_REQUEST_CODE -> {
                    (data.extras?.get("data") as? Bitmap)?.let { adapter.addImage(it) }
                    return
                }
                PICK_PHOTO_REQUEST_CODE -> {
                    data.data?.let {
                        try {
                            val bitmap = if (Build.VERSION.SDK_INT < 28)
                                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                            else {
                                val source = ImageDecoder.createSource(requireActivity().contentResolver, it)
                                ImageDecoder.decodeBitmap(source)
                            }
                            adapter.addImage(bitmap)
                        } catch (e: IOException) {
                            Toast.makeText(context, "Не удалось открыть файл", Toast.LENGTH_SHORT).show()
                        } catch (e: FileNotFoundException) {
                            Toast.makeText(context, "Файл не найден", Toast.LENGTH_SHORT).show()
                        }
                    }
                    return
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onAddClick() {
        AlertDialog.Builder(context).setItems(arrayOf("Сделать снимок", "Выбрать из галереи")) { _, which ->
            when (which) {
                0 -> takePhoto()
                1 -> pickPhoto()
            }
        }.show()
    }

    private fun pickPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE)
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
        else
            Toast.makeText(context, "Камера не поддерживается", Toast.LENGTH_SHORT).show()
    }
}