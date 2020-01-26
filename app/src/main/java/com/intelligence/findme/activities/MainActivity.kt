package com.intelligence.findme.activities

import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.intelligence.findme.R
import com.intelligence.findme.fragments.BottomSheetFragment
import com.intelligence.findme.fragments.ServiceListFragment
import com.intelligence.findme.models.ProviderResponse
import com.intelligence.findme.retrofit.RetrofitClient
import com.intelligence.findme.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initializeStuff()

        floatingActionButton.setOnClickListener {
            sendRequest()
            changeCameraPosition(LatLng(-1.2921, 36.8219))//For testing only
            showBottomSheetDialogFragment()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Toast.makeText(this@MainActivity, "Expanded", Toast.LENGTH_SHORT).show()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Toast.makeText(this@MainActivity, "Collapsed", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        })


        edt_main_search.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

                if (savedInstanceState == null) {
                    supportFragmentManager
                        .beginTransaction()
                        .add(
                            R.id.fragment_container,
                            ServiceListFragment.newInstance(),
                            "serviceList"
                        )
                        .commit()
                }
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun initializeStuff() {

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

        // Add a marker in Nairobi and move the camera
        val nairobi = LatLng(-1.2921, 36.8219)
        mMap.addMarker(MarkerOptions().position(nairobi).title("Marker in Nairobi"))

        changeCameraPosition(nairobi)

        mMap.uiSettings.isMapToolbarEnabled = false

        val location = Location("")
        location.latitude = nairobi.latitude
        location.longitude = nairobi.longitude

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

    private fun changeCameraPosition(target: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(target)
            .tilt(60F)
            .zoom(11F)
            .bearing(0F)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}
