package com.intelligence.findme.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.intelligence.findme.R
import com.intelligence.findme.adapters.mFragmentPagerAdapter
import com.intelligence.findme.util.Utils.Companion.logThis
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: FragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewPager = findViewById<ViewPager>(R.id.registerViewPager)
        adapter = mFragmentPagerAdapter(supportFragmentManager)
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

    private fun prepareData() {
        //TODO: Submit all entered data
        startActivity(Intent(this, MainActivity::class.java));
    }

    override fun onBackPressed() {
        if (viewPager.currentItem > 0) {
            viewPager.currentItem = viewPager.currentItem - 1
        } else {
            super.onBackPressed()
        }
    }

}
