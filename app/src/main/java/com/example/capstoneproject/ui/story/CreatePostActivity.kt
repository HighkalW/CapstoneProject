package com.example.capstoneproject.ui.story

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog.show
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityCreatePostBinding
import com.example.capstoneproject.models.Post
import com.example.capstoneproject.models.User
import com.example.capstoneproject.util.MediaUtil
import com.example.capstoneproject.util.animateVisibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.File

class CreatePostActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CreatePostActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_TOKEN = "extra_token"
    }

    private lateinit var binding: ActivityCreatePostBinding
    private var imageUri: Uri? = null
    private var getFile: File? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }

        binding.btnPost.setOnClickListener {
            val text = binding.postText.text.toString()

            if (TextUtils.isEmpty(text)) {
                Toast.makeText(
                    this,
                    "Description cannot be empty.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            addPost(text)
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


    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        MediaUtil.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@CreatePostActivity,
                getString(R.string.author),
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
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
                        LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = getString(R.string.img_type)
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = MediaUtil.uriToFile(selectedImg, this)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun addPost(text: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Users")
//                fetching the document details from firestore
            .document(FirebaseAuth.getInstance().currentUser?.uid!!).get()
            .addOnCompleteListener {
//                changing the firebase result to User's class object
                val user = it.result?.toObject(User::class.java)
//                Storage Reference: Firebase Storage
                val storage = FirebaseStorage.getInstance().reference.child("Images")
                    .child(FirebaseAuth.getInstance().currentUser?.email.toString() + "_" + System.currentTimeMillis() + ".jpg")
//                Upload task: Passing the uri of the image file which have to be upload to the firebase storage
                val uploadTask = storage.putFile(imageUri!!)
//                Uploading to Firebase
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        Log.d("Upload Task", task.exception.toString())
                    }
                    storage.downloadUrl
                }.addOnCompleteListener { urlTaskCompleted ->
                    if (urlTaskCompleted.isSuccessful) {
                        val downloadUri = urlTaskCompleted.result
//                        Creating an object of Post data class
                        val post =
                            Post(text, downloadUri.toString(), user!!, System.currentTimeMillis())
                        firestore.collection("Posts")
                            .document()
                            .set(post)
                            .addOnCompleteListener { posted ->
                                if (posted.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Posted Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Error occurred! Please Try again.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Log.d(TAG, urlTaskCompleted.exception.toString())
                    }
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            btnCamera.isEnabled = !isLoading
            btnGallery.isEnabled = !isLoading
            btnPost.isEnabled = !isLoading
            postText.isEnabled = !isLoading
            switchLocation.isEnabled = !isLoading

            if (isLoading) {
                viewProgressbar.animateVisibility(true)
            } else {
                viewProgressbar.animateVisibility(false)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*
        * Getting & Setting the Image Uri
        * */
        when (resultCode) {
            RESULT_OK -> {
                showLoading(true)
                Toast.makeText(
                    this,
                    "Task Succed",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    this,
                    "Task Cancelled",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}