package com.intelligence.findme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.intelligence.findme.R
import com.intelligence.findme.adapters.ServiceAdapter.ServiceViewHolder
import com.intelligence.findme.customfonts.TextViewHelveticaLight
import com.intelligence.findme.models.Service
import com.intelligence.findme.util.GlideApp
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ServiceAdapter(
    private val context: Context,
    private val serviceArrayList: ArrayList<Service>
) : RecyclerView.Adapter<ServiceViewHolder>() {

    private var searchInterface: SearchInterface? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val v = inflater.inflate(R.layout.item_service, parent, false)
        initializeSearchInterface()
        return ServiceViewHolder(v)
    }

    private fun initializeSearchInterface() {
        searchInterface = if (context is SearchInterface) {
            context
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service: Service = serviceArrayList[position]

        GlideApp.with(context)
            .load(service.image_url)
            .placeholder(R.drawable.ic_history)
            .fallback(R.drawable.ic_history)
            .into(holder.imageView)

        holder.tvContractorCount.text = service.contractorCount.toString() + " contractors"
        holder.tvServiceName.text = service.category
        holder.itemService.setOnClickListener {
            showAutoSearchDialog(position, service.profession)
        }

    }

    private fun showAutoSearchDialog(position: Int, profession: String) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Auto Search For $profession?")
        builder.setMessage("Would you like me to automatically locate nearest $profession")
        builder.setPositiveButton("Yes") { dialog, _ ->
            if (searchInterface != null)
                searchInterface!!.yesButtonClicked(profession)
            dialog.dismiss()
        }

        builder.setNegativeButton("No, View list") { dialog, _ ->
            if (searchInterface != null)
                searchInterface!!.yesButtonClicked(profession)
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun getItemCount(): Int {
        return serviceArrayList.size
    }

    class ServiceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: CircleImageView
        var tvServiceName: TextViewHelveticaLight
        var tvContractorCount: TextViewHelveticaLight
        var itemService: MaterialCardView

        init {
            imageView = itemView.findViewById(R.id.img_service)
            tvServiceName = itemView.findViewById(R.id.tv_service_name)
            tvContractorCount = itemView.findViewById(R.id.tv_contractor_count_for_service)
            itemService = itemView.findViewById(R.id.service_item)
        }
    }

    interface SearchInterface {
        fun yesButtonClicked(service: String)

        fun noButtonClicked(service: String)
    }
}