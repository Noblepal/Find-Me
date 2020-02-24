package com.intelligence.findme.util

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    const val ALL_SERVICES_TOKEN = "8JPqEv%qaU60g&KKTaotK"
    const val NEARBY_CONTRACTORS_TOKEN = "p*7ns6iVM%z&0FD9wTpxH"
    const val NEARBY_CONTRACTORS_BY_SERVICE = "0@lDD!2hc%h%rRjl91XOF"
    const val REGISTER_USER_TOKEN = "uIcrpOQ3w1Me%7JsT%wNY"
    const val LOGIN_USER_TOKEN = "%7*za1jlPXAm4%1WRzChT"
    const val UPDATE_USER_LOCATION_TOKEN = "Z#1qi2^qZ*pwoyg0268ZH"
    const val TAG = "Utils"

    fun hideView(v: View?) {
        if (v?.visibility == View.VISIBLE) {
            v.visibility = View.GONE
        }
    }

    fun showView(v: View?) {
        if (v?.visibility == View.GONE || v?.visibility == View.INVISIBLE) {
            v.visibility = View.VISIBLE
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager =
            (activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)!!
        if (inputMethodManager.isActive) {
            if (activity.currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    0
                )
            }
        }
    }

    fun md5(s: String): String {
        try { // Create MD5 Hash
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()
            // Create Hex String
            val hexString = StringBuilder()
            for (b in messageDigest) hexString.append(
                Integer.toHexString(
                    0xFF and b.toInt()
                )
            )
            Log.d(TAG, "md5: $hexString")
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun logThis(type: Int, TAG: String, message: String) {
        when (type) {
            0 -> {
                Log.d(TAG, message)
            }
            1 -> {
                Log.e(TAG, message)
            }
        }
    }

    fun formatDate(date: String): String? {
        val dateFormat = SimpleDateFormat("MMM, dd yyyy")
        val fromInput = SimpleDateFormat("yyyy-MM-dd")
        var reformattedStr = ""
        var mDate = ""
        val spacePos = date.indexOf(" ")
        if (spacePos > 0) {
            mDate = date.substring(0, spacePos)
        }
        try {
            val fromUser: Date = fromInput.parse(mDate)
            reformattedStr = dateFormat.format(fromUser)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return reformattedStr

    }
}