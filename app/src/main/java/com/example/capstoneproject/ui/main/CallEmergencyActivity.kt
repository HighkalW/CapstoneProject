package com.example.capstoneproject.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.capstoneproject.ui.detail.FeedFragment


class CallEmergencyActivity : AppCompatActivity() {

    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf<String>(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hasPermissions(this, *PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }

        performPhoneCall("112")
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


    private fun performPhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        try {
            startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(
                this,
                "Something went wrong! Please try again.",
                Toast.LENGTH_LONG
            ).show()
            Log.d(FeedFragment.TAG, "Failed to call")
        }
    }

}