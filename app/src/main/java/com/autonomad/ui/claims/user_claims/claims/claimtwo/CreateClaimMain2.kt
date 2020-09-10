package com.autonomad.ui.claims.user_claims.claims.claimtwo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.autonomad.R
import com.autonomad.utils.BaseFragment
import com.autonomad.data.models.claim_user.ServiceCreate
import com.autonomad.data.models.claims.ServiceModel
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.fragment_create_twomainclaim.*
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger

class CreateClaimMain2 : BaseFragment() {
    private val viewModel by viewModels<AddOfferImageViewModel>()
    private val adapter = AddOfferImageAdapter(::onAddClick)

    companion object {
        private const val PICK_PHOTO_REQUEST_CODE = 7884
        private const val TAKE_PHOTO_REQUEST_CODE = 8466
    }

    private var serviceCreateModel: ServiceCreate? = null
    private var claimServiceModel: ServiceModel? = null
    private var isEdit: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_twomainclaim, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceCreateModel = Gson().fromJson(arguments?.getString("serviceCreateModel"), ServiceCreate::class.java)
        claimServiceModel = Gson().fromJson(arguments?.getString("claimServiceModel"), ServiceModel::class.java)
        if (claimServiceModel != null) isEdit = true

        rv_images.adapter = adapter

        if (isEdit) claimServiceModel?.let { setEditInitialViews(it) }

        btn_next_claim.setOnClickListener {
            adapter.getImages().also {
                when {
                    edit_title.text?.isEmpty()!! -> {
                        Toast.makeText(context, "Напишите название заказа", Toast.LENGTH_SHORT)
                            .show()
                    }
                    edit_descrip.text?.isEmpty()!! -> {
                        Toast.makeText(context, "Напишите описание заказа", Toast.LENGTH_SHORT)
                            .show()
                    }
                    it.isEmpty() -> {
                        Toast.makeText(context, "Выберите фотографии", Toast.LENGTH_SHORT).show()
                    }
                    edit_budzhet.text?.isEmpty()!! -> {
                        Toast.makeText(context, "Напишите стоимость заказа", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        btn_next_claim.apply {
                            background = ContextCompat.getDrawable(
                                context,
                                R.drawable.background_grey_8_16dp_rectangle
                            )
                            setTextColor(ContextCompat.getColor(context, R.color.grey_6))
                            isFocusable = false
                            isClickable = false
                            isEnabled = false
                        }
                        proggr.apply {
                            visibility = View.VISIBLE
                        }

                        viewModel.upload(it)
                    }
                }
            }
        }

        val uploadedImagesList: ArrayList<Int>? = ArrayList()
        viewModel.uplodka.observe(viewLifecycleOwner) {
            uploadedImagesList?.add(it.id)
        }

        viewModel.mResult.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    proggr.apply {
                        visibility = View.GONE
                    }
                    btn_next_claim.apply {
                        background =
                            ContextCompat.getDrawable(context, R.drawable.background_blue_rectangle)
                        setTextColor(ContextCompat.getColor(context, R.color.grey_9))
                        isFocusable = true
                        isClickable = true
                        isEnabled = true
                    }

                    Toast.makeText(context, "Фотографии успешно загружены", Toast.LENGTH_SHORT)
                        .show()

                    serviceCreateModel?.apply {
                        title = edit_title.text.toString()
                        description = edit_descrip.text.toString()
                        images = uploadedImagesList
                        budget = edit_budzhet.text.toString().toLong()
                        negotiablePrice = cbNegotiable.isChecked
                    }

                    viewModel.uploads = AtomicInteger(0)
                    viewModel.failures = AtomicInteger(0)
                    val bundle = bundleOf("serviceCreateModel" to Gson().toJson(serviceCreateModel),
                        "claimServiceModel" to Gson().toJson(claimServiceModel))
                    findNavController().navigate(R.id.action_claimtwomain_to_threeclaim, bundle)

                }
                -1 -> {
                    proggr.apply {
                        visibility = View.GONE
                    }
                    btn_next_claim.apply {
                        background =
                            ContextCompat.getDrawable(context, R.drawable.background_blue_rectangle)
                        setTextColor(ContextCompat.getColor(context, R.color.grey_9))
                        isFocusable = true
                        isClickable = true
                        isEnabled = true
                    }
                    Toast.makeText(context, "Не удалось загрузить фотографии", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    proggr.apply { visibility = View.GONE }
                    btn_next_claim.apply {
                        background =
                            ContextCompat.getDrawable(context, R.drawable.background_blue_rectangle)
                        setTextColor(ContextCompat.getColor(context, R.color.grey_9))
                        isFocusable = true
                        isClickable = true
                        isEnabled = true
                    }
                    Toast.makeText(
                        context,
                        "Не удалось загрузить некоторые фотографии",
                        Toast.LENGTH_SHORT
                    ).show()

                    serviceCreateModel?.apply {
                        title = edit_title.text.toString()
                        description = edit_descrip.text.toString()
                        images = uploadedImagesList
                        budget = edit_budzhet.text.toString().toLong()
                        negotiablePrice = cbNegotiable.isChecked
                    }

                    val bundle = bundleOf("serviceCreateModel" to Gson().toJson(serviceCreateModel),
                        "claimServiceModel" to Gson().toJson(claimServiceModel))
                    findNavController().navigate(R.id.action_claimtwomain_to_threeclaim, bundle)
                }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                TAKE_PHOTO_REQUEST_CODE -> {
                    (data.extras?.get("data") as? Bitmap)?.let {
                        adapter.addImage(it)
                    }
                    return
                }
                PICK_PHOTO_REQUEST_CODE -> {
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
                            adapter.addImage(bitmap)
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
        } else {
            viewModel.result.removeObservers(this)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onAddClick() {
        AlertDialog.Builder(context)
            .setItems(arrayOf("Сделать снимок", "Выбрать из галереи")) { _, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> pickPhoto()
                }
            }.show()
    }

    private fun pickPhoto() {
        Dexter.withContext(context).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest();
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(context, "Дайте доступ в галерею", Toast.LENGTH_SHORT).show()
                }

            }).check()

    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null)
            Dexter.withContext(context).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        p0?.let {
                            if (p0.areAllPermissionsGranted()) {
                                startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
                            } else {
                                Toast.makeText(context, "Дайте доступ к камере в настройках", Toast.LENGTH_SHORT).show()
                            }
                         }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest();
                    }

                }).check()
        else
            Toast.makeText(context, "Камера не поддерживается", Toast.LENGTH_SHORT).show()
    }

    override fun initialise() {
        ic_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun setObservers() {

    }

    override fun setAdapter() {
        val services = ArrayList<String>()
        services.add(("Ремонт авто"))
    }

    override fun setOnClickListener() {

    }

    private fun setEditInitialViews(claimServiceModel: ServiceModel) {
        edit_title.setText(claimServiceModel.title ?: "")
        edit_descrip.setText(claimServiceModel.description ?: "")
        edit_budzhet.setText(claimServiceModel.budget?.toString() ?: "")
        cbNegotiable.isChecked = claimServiceModel.negotiablePrice
        claimServiceModel.images?.forEach {
            Picasso.get().load(it.image).into(object: Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let {img -> adapter.addImage(img) }
                }

            })

        }
    }


}