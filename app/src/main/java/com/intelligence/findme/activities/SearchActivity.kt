package com.intelligence.findme.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.intelligence.findme.R
import com.intelligence.findme.adapters.SpinnerAdapter
import com.intelligence.findme.models.Service
import com.intelligence.findme.models.ServiceResponse
import com.intelligence.findme.retrofit.RetrofitClient
import com.intelligence.findme.util.Utils
import com.intelligence.findme.util.Utils.Companion.hideView
import com.intelligence.findme.util.Utils.Companion.showView
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private val TAG = "SearchActivity"
    //TODO: Convert this activity into a FragmentDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setUpSpinner()

    }

    private fun setUpSpinner() {
        showView(progress_s)
        RetrofitClient.instance.getAllServices(Utils.ALL_SERVICES_TOKEN)
            .enqueue(object : Callback<ServiceResponse> {
                override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                    hideView(progress_s)
                    Log.d(TAG, "Failed: ${t.message}")
                }

                override fun onResponse(
                    call: Call<ServiceResponse>,
                    response: Response<ServiceResponse>
                ) {
                    hideView(progress_s)
                    val services: List<Service> = response.body()!!.services
                    //Log.d(TAG, "Services: ${services.toString()}")
                    val spinnerAdapter = SpinnerAdapter(
                        this@SearchActivity,
                        services
                    )
                    services_spinner?.adapter = spinnerAdapter
                }
            })

        services_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@SearchActivity, "Selected: $position", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }

}