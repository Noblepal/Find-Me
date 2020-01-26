package com.intelligence.findme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val v = inflater.inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(v)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service: Service = serviceArrayList[position]

        GlideApp.with(context)
            .load(service.image_url)
            .placeholder(R.drawable.ic_history)
            .fallback(R.drawable.ic_history)
            .into(holder.imageView)

        holder.tvContractorCount.text = service.contractorCount.toString()
        holder.tvServiceName.text = service.category

    }

    override fun getItemCount(): Int {
        return serviceArrayList.size
    }

    class ServiceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: CircleImageView
        var tvServiceName: TextViewHelveticaLight
        var tvContractorCount: TextViewHelveticaLight

        init {
            imageView = itemView.findViewById(R.id.img_service)
            tvServiceName = itemView.findViewById(R.id.tv_service_name)
            tvContractorCount = itemView.findViewById(R.id.tv_contractor_count_for_service)
        }
    }

}