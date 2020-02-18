package com.intelligence.findme.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.intelligence.findme.R
import com.intelligence.findme.adapters.ServiceAdapter
import com.intelligence.findme.fragments.BottomSheetFragment
import com.intelligence.findme.fragments.ProvidersMiniFragment
import com.intelligence.findme.fragments.ServiceListFragment
import com.intelligence.findme.models.Provider
import com.intelligence.findme.models.ProviderResponse
import com.intelligence.findme.retrofit.RetrofitClient
import com.intelligence.findme.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), OnMapReadyCallback, ServiceAdapter.SearchInterface {

    private lateinit var mMap: GoogleMap
    private val TAG = "MainActivity"
    private var isAlreadyFetched = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private lateinit var serviceListFragment: ServiceListFragment
    private lateinit var mProgress: ProgressDialog
    private val PERMISSION_REQUEST_CODE = 110
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var mLastLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()
        checkGPSPermission()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initializeStuff()

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }

                }
            }

        })


        edt_main_search.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                serviceListFragment = ServiceListFragment.newInstance()
                if (savedInstanceState == null && !isAlreadyFetched) {
                    supportFragmentManager
                        .beginTransaction()
                        .add(
                            R.id.fragment_container,
                            serviceListFragment,
                            "serviceList"
                        )
                        .commit()
                    isAlreadyFetched = true
                }
            }
        }
    }

    private fun initializeStuff() {
        floatingActionButton.setOnClickListener {
            if (checkGPSPermission())
            //changeCameraPosition(mLastLocation)
            else
                showSnackBar(
                    "GPS not enabled. This application will not work correctly",
                    -2,
                    true,
                    "Enable"
                )
        }

        getLastKnownLocation()
    }

    private fun sendRequest() {
        RetrofitClient.instance.getProvidersWithinRadius(
            Utils.NEARBY_CONTRACTORS_TOKEN,
            "-1.18765160",
            "36.77939290"
        )
            .enqueue(object : Callback<ProviderResponse> {
                override fun onFailure(call: Call<ProviderResponse>, t: Throwable) {
                    Log.e(TAG, t.message)
                }

                override fun onResponse(
                    call: Call<ProviderResponse>,
                    response: Response<ProviderResponse>
                ) {
                    Log.d(TAG, response.body()?.message)
                    Log.d(TAG, response.body()?.contractors.toString())
                    val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                    alertDialogBuilder.setTitle(response.body()?.message)
                    alertDialogBuilder.setMessage(response.body()?.contractors.toString())
                    alertDialogBuilder.setPositiveButton("Okay") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog: AlertDialog = alertDialogBuilder.create()
                    dialog.show()
                }

            })
    }

    private fun showBottomSheetDialogFragment() {
        val bottomSheetFragment =
            BottomSheetFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        customizeMapStyle(googleMap)
        mMap = googleMap

        if (checkGPSPermission()) {
            //changeCameraPosition(mLastLocation)
        } else {
            requestGPSPermission()
        }

        mMap.uiSettings.isMapToolbarEnabled = false
    }


    private fun changeCameraPosition(target: Location?) {
        val targetLatLng = LatLng(target!!.latitude, target.longitude)
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    mLastLocation!!.latitude,
                    mLastLocation!!.longitude
                )
            )
        )
        val cameraPosition = CameraPosition.Builder()
            .target(targetLatLng)
            .tilt(60F)
            .zoom(11F)
            .bearing(0F)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun checkGPSPermission(): Boolean {
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return gpsEnabled
    }

    private fun requestGPSPermission() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Enable GPS")
        dialogBuilder.setMessage("This application requires GPS to be enabled. Please enable in settings")
        dialogBuilder.setPositiveButton("Enable") { dialog, _ ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            showSnackBar(
                "GPS not enabled. This application will not work correctly",
                -2,
                true,
                "Enable"
            )
        }
    }

    private fun showSnackBar(s: String, LENGTH: Int, hasAction: Boolean, actionText: String) {
        val snackBar: Snackbar = Snackbar.make(bottomSheet, s, LENGTH)
        if (hasAction) {
            snackBar.setAction(actionText) {
                when (actionText) {
                    "Enable" -> {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            }
        }
        snackBar.show()
    }

    private fun getLastKnownLocation(): Boolean {
        var isLocationFound = false
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                mLastLocation = task.result
                Log.e(TAG, "Lat ${mLastLocation!!.latitude}")
                Log.e(TAG, "Lng ${mLastLocation!!.longitude}")
                isLocationFound = true
                changeCameraPosition(mLastLocation)
            } else {
                //Toast.makeText(this, "Location not found", Toast.LENGTH_LONG).show()
                isLocationFound = false
                //getLastKnownLocation()
            }
        }
        return isLocationFound
    }


    private fun customizeMapStyle(googleMap: GoogleMap) {
        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.snazzy_blue_map)
            )

            if (!success) {
                Log.e(TAG, "Failed to set map style")
            }

        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Resource not found: $e")
        }
    }

    override fun yesButtonClicked(service: String) {
        removeFragment(serviceListFragment)
        collapseBottomSheetLayout()
        findNearestContractor(service)
        showProgressDialog("Searching for $service nearby", true)
        //Toast.makeText(applicationContext, "Yes: $service", Toast.LENGTH_LONG).show()
    }

    private fun findNearestContractor(s: String) {
        RetrofitClient.instance.getNearestContractor(
            Utils.NEARBY_CONTRACTORSBY_SERVICE,
            s,
            mLastLocation!!.latitude,
            mLastLocation!!.longitude
        ).enqueue(object : Callback<ProviderResponse> {
            override fun onFailure(call: Call<ProviderResponse>, t: Throwable) {
                dismissProgressBar()
                Toast.makeText(applicationContext, "Error ${t.message}", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(
                call: Call<ProviderResponse>,
                response: Response<ProviderResponse>
            ) {
                if (response.isSuccessful) {
                    if (!response.body()!!.error) {
                        dismissProgressBar()
                        var contractors: List<Provider> = response.body()!!.contractors
                        displayContractorMarkers(contractors)
                        alsoLoadNewFragmentAndPopulateRecyclerViewWithData(contractors)
                        //Toast.makeText(applicationContext, "${response.body()}", Toast.LENGTH_LONG).show()
                    } else {
                        dismissProgressBar()
                        Toast.makeText(applicationContext, "No $s found", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    dismissProgressBar()
                    Toast.makeText(
                        applicationContext,
                        "Could not find any $s nearby",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        })
    }

    private fun alsoLoadNewFragmentAndPopulateRecyclerViewWithData(contractors: List<Provider>) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fragment_container,
                ProvidersMiniFragment.newInstance(contractors),
                "providerList"
            )
            .commit()
    }

    private fun displayContractorMarkers(contractors: List<Provider>) {
        var smallestDistance = contractors[0].distance
        for (contractor in contractors) {
            smallestDistance = if (contractor.distance < smallestDistance) {
                contractor.distance
            } else {
                smallestDistance
            }
            mMap.addMarker(
                MarkerOptions().position(LatLng(contractor.lat, contractor.lng)).title(
                    contractor.full_name
                )
            )

        }
    }

    private fun showProgressDialog(s: String, isCancelable: Boolean) {
        val zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        llProgress.visibility = View.VISIBLE
        llProgress.startAnimation(zoomIn)
        /*mProgress = ProgressDialog(this)
        mProgress.setMessage(s)
        mProgress.setCancelable(isCancelable)
        mProgress.show()*/
    }

    private fun dismissProgressBar() {
        if (llProgress.visibility == View.VISIBLE) {
            val zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
            llProgress.visibility = View.INVISIBLE
            llProgress.startAnimation(zoomOut)
        }
    }

    private fun collapseBottomSheetLayout() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
        isAlreadyFetched = false
    }

    override fun noButtonClicked(service: String) {
        Toast.makeText(applicationContext, "No: $service", Toast.LENGTH_LONG).show()
    }

    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation()
            } else {
                Toast.makeText(this, "Permission not granted, Exiting", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }
    }

}
