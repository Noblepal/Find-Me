package com.intelligence.findme.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.intelligence.findme.R
import com.intelligence.findme.adapters.MyFragmentPagerAdapter
import com.intelligence.findme.models.RegisterResponse
import com.intelligence.findme.retrofit.RetrofitClient
import com.intelligence.findme.util.SharedPrefsManager
import com.intelligence.findme.util.Utils.REGISTER_USER_TOKEN
import com.intelligence.findme.util.Utils.hideView
import com.intelligence.findme.util.Utils.logThis
import com.intelligence.findme.util.Utils.showView
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: MyFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewPager = findViewById<ViewPager>(R.id.registerViewPager)
        adapter = MyFragmentPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        floating_next.setOnClickListener { nextOrPreviousFragment(true) }

    }

    private fun nextOrPreviousFragment(isNext: Boolean) {
        logThis(0, TAG, "isNext: $isNext")
        var position = viewPager.currentItem
        logThis(0, TAG, "Position: $position")
        if (isNext) {
            if (position < 2) {
                position++
                viewPager.currentItem = position
            } else {
                logThis(0, TAG, "End of viewpager")
                prepareData()
            }
        } else {
            if (position > 0) {
                position--
                viewPager.currentItem = position
            } else {
                logThis(0, TAG, "Start of viewpager")
            }
        }
    }

    //TODO: Not tested yet!!
    private fun prepareData() {
        showView(registerProgressBar)
        floating_next.setImageResource(0)
        val userData = getAllData()
        RetrofitClient.instance.registerUser(
            REGISTER_USER_TOKEN,
            userData[0],
            userData[1],
            userData[2]
        ).enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                logThis(0, "onFailure", "Error: ${t.localizedMessage}")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                logThis(0, "OnResponse", "Successfully registered")
                startActivity(Intent(applicationContext, MainActivity::class.java))
                floating_next.setImageResource(R.drawable.ic_next)
                hideView(registerProgressBar)
            }

        })
    }

    private fun getAllData(): Array<String> {
        return SharedPrefsManager.getInstance(this@RegisterActivity)!!.getTempData()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem > 0) {
            viewPager.currentItem = viewPager.currentItem - 1
        } else {
            super.onBackPressed()
        }
    }

}
