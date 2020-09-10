package com.autonomad.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.autonomad.R
import com.autonomad.data.models.login.Profile
import com.autonomad.ui.bottom_sheet.penalty.AddDriverViewModel
import com.autonomad.utils.Methods
import com.autonomad.utils.UinController
import com.autonomad.utils.navigateBack
import com.autonomad.utils.toast
import com.google.gson.Gson
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.fragment_profile_user_page.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

class UserPageFragment : Fragment(R.layout.fragment_profile_user_page) {
    private val viewModel by viewModels<AddDriverViewModel>()
    private val viewModeltwo by viewModels<UserPageViewModel>()

    private var gender: Int = 0
    private var uinController: UinController? = null

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )
        } else {
            uploadImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        try {
            if (requestCode == 111) {
                if (hasAllPermissionsGranted(grantResults)) {
                    uploadImage()
                } else {
                    toast("Дайте доступ к камере")
                }
            }
        } catch (e: Exception) {
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun hasAllPermissionsGranted(@NonNull grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_back.setOnClickListener { activity?.onBackPressed() }
        navigateBack(R.id.action_global_profile)

        val profile = Gson().fromJson(arguments?.getString("profile"), Profile::class.java)
        profile?.let {

            Picasso.get()
                .load(it.thumbnail)
                .placeholder(R.drawable.ic_profile_male)
                .into(profile_icon)
            phone_number.setText(it.phone)
            emaill.setText(it.email)
            nammme.setText(it.firstName)
            lastnamme.setText(it.lastName)
            patronommik.setText(it.patronymic)
            etBirthday.setText(it.birthdate)
        }

        uinController = UinController(uin_layout) {
            if (it != null) viewModel.penaltySearch(it)
        }
        viewModel.driver.observe(viewLifecycleOwner) {
            proggres.visibility = if (it.isLoading) View.VISIBLE else View.GONE
            text_error.isVisible = !it.isSuccess

            it.onSuccess {
                nammme.setText(fullName.substringBefore(" "))
                lastnamme.setText(fullName.substringAfter(" ").substringBefore(" "))
                patronommik.setText(fullName.substringAfterLast(" "))
            }

        }

        save.setOnClickListener {
            viewModeltwo.patchProfile(
                Methods.getToken(),
                emaill.text.toString(),
                nammme.text.toString(),
                lastnamme.text.toString(),
                patronommik.text.toString(),
                gender,
                etBirthday.text.toString()
            )
        }

        viewModeltwo.booleeaan.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Данные успешно изменены", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else {
                Toast.makeText(context, "Данные не были изменены", Toast.LENGTH_SHORT).show()
            }
        })
        viewModeltwo.dataLoading.observe(viewLifecycleOwner) {
            container.isVisible = !it
            progress_bar.isVisible = it
        }

        val languages = resources.getStringArray(R.array.array_pol)

        if (spinnerr != null) {
            val adapter = CustomSpinnerAdapter(
                context as Context,
                android.R.layout.simple_spinner_item,
                languages,
                "Не выбран"
            )
            spinnerr.adapter = adapter
            spinnerr.setSelection(0, false)



            spinnerr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    gender = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        etBirthday.addTextChangedListener(object : TextWatcher {

            var sb: StringBuilder = StringBuilder("")

            var _ignore = false

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (_ignore) {
                    _ignore = false
                    return
                }

                sb.clear()
                sb.append(
                    if (s!!.length > 10) {
                        s.subSequence(0, 10)
                    } else {
                        s
                    }
                )

                if (sb.lastIndex == 4) {
                    if (sb[4] != '-') {
                        sb.insert(4, "-")
                    }
                } else if (sb.lastIndex == 7) {
                    if (sb[7] != '-') {
                        sb.insert(7, "-")
                    }
                }

                _ignore = true
                etBirthday.setText(sb.toString())
                etBirthday.setSelection(sb.length)

            }
        })


        //phone_number.text.toString().replace("(", "").replace(")", "").replace(" ", "")
        phone_number.doOnTextChanged { text, start, count, after ->
            val text = phone_number.text.toString()
            if (text.endsWith(")") || text.endsWith(" "))
                return@doOnTextChanged
            else if (text.endsWith("(")) {
                phone_number.setText("")
            }
            when (phone_number.text.length) {
                1 -> {
                    phone_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            "+7("
                        ).toString()
                    )
                    phone_number.setSelection(phone_number.text.length)
                }
                7 -> {
                    phone_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            ")"
                        ).toString()
                    )
                    phone_number.setSelection(phone_number.text.length)
                }
                11 -> {
                    phone_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            " "
                        ).toString()
                    )
                    phone_number.setSelection(phone_number.text.length)
                }
                14 -> {
                    phone_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1,
                            " "
                        ).toString()
                    )
                    phone_number.setSelection(phone_number.text.length)
                }
                17 -> {
                    phone_number.setText(
                        StringBuilder(text).deleteCharAt(
                            text.length - 1
                        ).toString()
                    )
                    phone_number.setSelection(phone_number.text.length)
                }
            }
        }

        addPhoto_tv.setOnClickListener {
            requestPermission()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                789 -> {
                    (data.extras?.get("data") as? Bitmap)?.let {
//                        profile_icon.setImageBitmap(it)
                        val transformation: Transformation = RoundedTransformationBuilder()
                            .borderColor(Color.BLACK)
                            .borderWidthDp(0f)
                            .cornerRadiusDp(500f)
                            .oval(false)
                            .build()

                        Picasso.get()
                            .load(getImageUri(it))
                            .transform(transformation)
                            .placeholder(R.drawable.ic_profile_male)
                            .into(profile_icon)
                        viewModeltwo.setBitmap(it)
                    }
                    return
                }
                456 -> {
                    data.data?.let {
                        try {
                            val bitmap = if (Build.VERSION.SDK_INT < 28)
                                MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    it
                                )
                            else {
                                val source =
                                    ImageDecoder.createSource(requireActivity().contentResolver, it)
                                ImageDecoder.decodeBitmap(source)
                            }
//                            profile_icon.setImageBitmap(bitmap)
                            val transformation: Transformation = RoundedTransformationBuilder()
                                .borderColor(Color.BLACK)
                                .borderWidthDp(0f)
                                .cornerRadiusDp(500f)
                                .oval(false)
                                .build()

                            Picasso.get()
                                .load(getImageUri(bitmap))
                                .transform(transformation)
                                .placeholder(R.drawable.ic_profile_male)
                                .into(profile_icon)
                            viewModeltwo.setBitmap(bitmap)
//                            adapter.addImage(bitmap)
                        } catch (e: IOException) {
                            Toast.makeText(context, "Не удалось открыть файл", Toast.LENGTH_SHORT)
                                .show()
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

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun uploadImage() {
        AlertDialog.Builder(context)
            .setItems(arrayOf("Сделать снимок", "Выбрать из галереи")) { _, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> pickPhoto()
                }
            }.show()
    }

    private fun pickPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 456)
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null)
            startActivityForResult(intent, 789)
        else
            Toast.makeText(context, "Камера не поддерживается", Toast.LENGTH_SHORT).show()
    }

}
