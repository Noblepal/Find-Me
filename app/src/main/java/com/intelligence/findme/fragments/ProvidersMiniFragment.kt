package com.intelligence.findme.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intelligence.findme.R
import com.intelligence.findme.adapters.ProviderAdapter
import com.intelligence.findme.models.Provider

class ProvidersMiniFragment : Fragment() {

    val TAG = "ProvidersMiniFragment"

    companion object {
        var providerArrayList: List<Provider> = ArrayList()
        fun newInstance(providers: List<Provider>): ProvidersMiniFragment {
            providerArrayList = providers
            return ProvidersMiniFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_providers_mini, container, false)
        getProviders(v)
        return v
    }

    private fun getProviders(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.providerMiniRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = ProviderAdapter(context!!, providerArrayList)

    }
}
