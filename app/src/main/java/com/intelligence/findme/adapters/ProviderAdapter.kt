package com.intelligence.findme.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.intelligence.findme.R
import com.intelligence.findme.activities.ContractorDetailActivity
import com.intelligence.findme.models.Provider
import com.intelligence.findme.util.GlideApp
import java.math.RoundingMode

class ProviderAdapter(
    private val context: Context,
    private val providerArrayList: List<Provider>
) : RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder>() {

    private var detailInterface: DetailInterface? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewHolder {
        val v = inflater.inflate(R.layout.item_mini_provider, parent, false)
        initializeSearchInterface()
        return ProviderViewHolder(v)
    }

    private fun initializeSearchInterface() {
        detailInterface = if (context is DetailInterface) {
            context
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: ProviderViewHolder, position: Int) {
        val provider: Provider = providerArrayList[position]

        GlideApp.with(context)
            .load(provider.profile_photo)
            .placeholder(R.drawable.ic_history)
            .fallback(R.drawable.ic_history)
            .into(holder.imageView)

        val distance = provider.distance.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        holder.distance.text = distance.toString() + " km"
        holder.providerName.text = provider.full_name
        holder.priceHour.text = provider.price
        holder.service.text = provider.service_type
        holder.item.setOnClickListener {
            context.startActivity(
                Intent(context, ContractorDetailActivity::class.java)
                    .putExtra("item", provider)
            )
        }
        /*holder.item.setOnClickListener {
            if (detailInterface != null)
                detailInterface!!.getProvider(provider)
        }*/
    }

    private fun showContactDialog(position: Int, profession: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Auto Search For $profession?")
        builder.setMessage("Would you like me to automatically locate nearest $profession")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setNegativeButton("No, View list") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun getItemCount(): Int {
        return providerArrayList.size
    }

    class ProviderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var item: MaterialCardView
        var imageView: ImageView
        var providerName: TextView
        var distance: TextView
        var service: TextView
        var priceHour: TextView

        init {
            item = itemView.findViewById(R.id.itemMiniProvider)
            imageView = itemView.findViewById(R.id.imgMiniProvider)
            providerName = itemView.findViewById(R.id.tvMiniContractorName)
            distance = itemView.findViewById(R.id.tvMiniDistance)
            service = itemView.findViewById(R.id.tvMiniContractorService)
            priceHour = itemView.findViewById(R.id.tvMiniPrice)
        }
    }

    interface DetailInterface {
        fun getProvider(provider: Provider)
    }
}
