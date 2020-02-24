package com.intelligence.findme.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.intelligence.findme.R
import com.intelligence.findme.models.Provider
import com.intelligence.findme.util.GlideApp
import de.hdodenhof.circleimageview.CircleImageView

class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        lateinit var provider: Provider
        fun newInstance(mProvider: Provider): BottomSheetFragment {
            provider = mProvider
            return BottomSheetFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.bottom_sheet, container, true)
        try {
            /* val bottomSheetDialog: Dialog = dialog!!
             val bottomSheet: View = bottomSheetDialog.findViewById(R.id.bottomSheet)
             BottomSheetBehavior.from(bottomSheet).peekHeight = 256*/

            val pIoHW = fragmentView.findViewById<TextView>(R.id.textViewHelveticaLight)
            val bottomSheetDriverName =
                fragmentView.findViewById<TextView>(R.id.bottomSheetDriverName)
            val bottomSheetVehicleModel =
                fragmentView.findViewById<TextView>(R.id.bottomSheetVehicleModel)
            val imgProviderProfile =
                fragmentView.findViewById<CircleImageView>(R.id.img_provider_profile)

            val s = provider.full_name + " is on his way"
            pIoHW.text = s
            bottomSheetDriverName.text = provider.full_name
            bottomSheetVehicleModel.text = provider.service_type

            GlideApp.with(imgProviderProfile)
                .load(provider.profile_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fallback(R.drawable.ic_account_circle)
                .placeholder(R.drawable.ic_account_circle)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(imgProviderProfile)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BottomSheetFragment", "Error inflating bottomSheet: ${e.localizedMessage}")
        }

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

    }

}
