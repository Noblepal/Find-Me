package com.intelligence.findme.fragments

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.intelligence.findme.R
import com.intelligence.findme.adapters.SpinnerAdapter
import com.intelligence.findme.models.Service
import com.intelligence.findme.models.ServiceResponse
import com.intelligence.findme.retrofit.RetrofitClient
import com.intelligence.findme.util.Utils
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchDialogFragment : DialogFragment() {
    private val TAG = "SearchActivity"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = layoutInflater.inflate(R.layout.activity_search, container)
        val requestBtn = v.findViewById<TextView>(R.id.btn_request_service)
        val edtDescription = v.findViewById<EditText>(R.id.edt_description)
        val tvTimePicker = v.findViewById<TimePicker>(R.id.time_picker)
        val spinnerServices = v.findViewById<Spinner>(R.id.services_spinner)
        val progressBar = v.findViewById<ProgressBar>(R.id.progress_s)

        requestBtn.setOnClickListener { v ->
            performSearch(tvTimePicker, spinnerServices, edtDescription)
        }

        setUpSpinner(spinnerServices, progressBar)

        return v
    }

    private fun performSearch(
        tvTimePicker: TimePicker?,
        spinnerServices: Spinner?,
        edtDescription: EditText?
    ) {

    }


    private fun setUpSpinner(
        spinnerServices: Spinner,
        progressBar: ProgressBar
    ) {
        Utils.showView(progressBar)
        RetrofitClient.instance.getAllServices("8JPqEv%qaU60g&KKTaotK")
            .enqueue(object : Callback<ServiceResponse> {
                override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                    Utils.hideView(progressBar)
                    Log.d(TAG, "Failed: ${t.message}")
                }

                override fun onResponse(
                    call: Call<ServiceResponse>,
                    response: Response<ServiceResponse>
                ) {
                    Utils.hideView(progressBar)
                    val services: List<Service> = response.body()!!.services
                    //Log.d(TAG, "Services: ${services.toString()}")
                    val spinnerAdapter = SpinnerAdapter(
                        activity!!.applicationContext,
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
                Toast.makeText(
                    activity!!.applicationContext,
                    "Selected: $position",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onResume() {
        super.onResume()
        val window: Window? = dialog!!.window
        val size = Point()
        val display: Display = window!!.getWindowManager().getDefaultDisplay()
        display.getSize(size)
        val width: Int = size.x
        window.setLayout((width * 0.75).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
    }
}