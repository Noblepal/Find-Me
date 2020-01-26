package com.intelligence.findme.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intelligence.findme.R
import com.intelligence.findme.adapters.ServiceAdapter
import com.intelligence.findme.models.Service
import com.intelligence.findme.models.ServiceResponse
import com.intelligence.findme.retrofit.RetrofitClient
import com.intelligence.findme.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceListFragment : Fragment() {

    var serviceArrayList: ArrayList<Service> = ArrayList()
    val TAG = "ServiceListFragment"

    companion object {
        fun newInstance(): ServiceListFragment {
            return ServiceListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        RetrofitClient.instance.getAllServices(Utils.ALL_SERVICES_TOKEN)
            .enqueue(object : Callback<ServiceResponse> {

                override fun onResponse(
                    call: Call<ServiceResponse>,
                    response: Response<ServiceResponse>
                ) {
                    if (response.isSuccessful) {
                        try {
                            val data: List<Service>? = response.body()?.services
                            serviceArrayList.addAll(data!!)
                        } catch (e: Exception) {
                            Log.d(TAG, "Line: 46")
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(context, "Error: " + response.errorBody(), Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                    Toast.makeText(context, "Error: " + t.message, Toast.LENGTH_LONG).show()
                }

            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_service_list, container, false)

        val activity = activity as Context
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewServices)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ServiceAdapter(activity, serviceArrayList)

        return view
    }
}