package com.example.capstoneproject.ui.story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.R
import com.example.capstoneproject.data.Result
import com.example.capstoneproject.databinding.ActivityStoryBinding
import com.example.capstoneproject.ui.map.MapsActivity
import com.example.capstoneproject.util.MediaUtil
import com.example.capstoneproject.util.animateVisibility
import com.example.submissionintermedieteakhir.ui.viewmodelfactory.StoryVMF
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


@Suppress("SameParameterValue")
class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var token: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(MapsActivity.EXTRA_TOKEN).toString()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        title = getString(R.string.upload_story_tittle)

        setupViewModel()
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                location = null
            }
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun setupViewModel() {
        val factory: StoryVMF = StoryVMF.getInstance(this)
        storyViewModel = ViewModelProvider(
            this,
            factory
        )[StoryViewModel::class.java]
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = getString(R.string.img_type)
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }
    private fun uploadImage() {
        if (getFile != null) {
            val description = binding.edtDesc.text.toString().trim()
            if (description.isEmpty()) {
                binding.edtDesc.error =
                    resources.getString(R.string.message_validation, "description")
            } else {
                showLoading(true)
                val file = MediaUtil.reduceFileImage(getFile as File)
                val descMedia = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    getString(R.string.photo),
                    file.name,
                    requestImageFile
                )
                var lat: RequestBody? = null
                var lon: RequestBody? = null
                if (location != null) {
                    lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                }
                storyViewModel.uploadStory(token, imageMultipart, descMedia, lat, lon)
                    .observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(true)
                                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                }
                                is Result.Error -> {
                                    showLoading(true)
                                    Toast.makeText(
                                        this,
                                        getString(R.string.failure_ket) + result.error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
            }
        } else {
            Toast.makeText(this, resources.getString(R.string.input_image), Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            btnCamera.isEnabled = !isLoading
            btnGallery.isEnabled = !isLoading
            btnUpload.isEnabled = !isLoading
            edtDesc.isEnabled = !isLoading
            switchLocation.isEnabled = !isLoading

            if (isLoading) {
                viewProgressbar.animateVisibility(true)
            } else {
                viewProgressbar.animateVisibility(false)
            }
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = MediaUtil.uriToFile(selectedImg, this@StoryActivity)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        MediaUtil.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@StoryActivity,
                getString(R.string.author),
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    this.location = loc
                } else {
                    binding.switchLocation.isChecked = false
                    Toast.makeText(
                        this,
                        resources.getString(R.string.loc_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }



    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_TOKEN = "extra_token"
    }
}