package com.intelligence.findme.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.intelligence.findme.R
import com.intelligence.findme.models.Provider
import com.intelligence.findme.util.GlideApp
import com.intelligence.findme.util.Utils.formatDate
import kotlinx.android.synthetic.main.activity_contractor_details.*

class ContractorDetailActivity : AppCompatActivity() {

    private lateinit var provider: Provider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contractor_details)
        setSupportActionBar(detailToolbar)
        detailToolbar.title = "Contractor Details"

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            provider = bundle.getSerializable("item") as Provider
            initializeData()
        }

        btnRequestContractorService.setOnClickListener {
            Toast.makeText(this, "Requesting service...", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("service_request", provider)
            startActivity(intent)
        }

    }

    private fun initializeData() {
        det_name.text = provider.full_name
        det_desc.text = provider.service_description
        tv_service_type.text = provider.service_type
        tv_county.text = provider.county
        val joinDate = "Joined - ${provider.reg_date}"
        tv_reg_date.text = formatDate(joinDate)
        GlideApp.with(contractor_pic)
            .load(provider.profile_photo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fallback(R.drawable.ic_account_circle)
            .placeholder(R.drawable.ic_account_circle)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(contractor_pic)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsMenuClosed(menu: Menu?) {
        super.onOptionsMenuClosed(menu)
    }

}