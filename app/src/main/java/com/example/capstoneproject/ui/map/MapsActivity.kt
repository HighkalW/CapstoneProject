package com.example.capstoneproject.ui.map

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityMapsBinding
import com.example.capstoneproject.ui.story.StoryViewModel
import com.example.capstoneproject.data.Result
import com.example.capstoneproject.util.MediaUtil
import com.example.submissionintermedieteakhir.ui.viewmodelfactory.StoryVMF
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var token: String
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(EXTRA_TOKEN).toString()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupViewModel()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(ContentValues.TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(ContentValues.TAG, "Can't find style. Error: ", exception)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setupMarker()
        setMapStyle()
        getMyLocation()
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    showMyMarker(loc)
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        resources.getString(R.string.loc_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showMyMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .icon(
                    MediaUtil.vectorToBitmap(R.drawable.ic_baseline_location_on_24,
                        Color.parseColor("#2D3D4F"), this))
                .title(getString(R.string.my_loc))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 5f))
    }



    private fun setupViewModel() {
        val factory: StoryVMF = StoryVMF.getInstance(this)
        mapsViewModel = ViewModelProvider(this, factory)[MapsViewModel::class.java]
    }
    private fun setupMarker() {
        mapsViewModel.getStories(token).observe(this){result ->
            if (result != null){
                when(result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        result.data.listStory.map {
                            if (it.lat != null && it.lon != null){
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(it.lat, it.lon))
                                        .title(it.name)
                                        .icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_BLUE))
                                        .snippet("${it.lat}, ${it.lon}")
                                )
                            }
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Failure : " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}