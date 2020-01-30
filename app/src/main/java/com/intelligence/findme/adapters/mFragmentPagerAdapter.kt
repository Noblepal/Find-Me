package com.intelligence.findme.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.intelligence.findme.fragments.PasswordFragment
import com.intelligence.findme.fragments.PhoneFragment
import com.intelligence.findme.fragments.UsernameFragment

class mFragmentPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val count = 3

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UsernameFragment.newInstance()
            1 -> fragment = PhoneFragment.newInstance()
            2 -> fragment = PasswordFragment.newInstance()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return count
    }
}