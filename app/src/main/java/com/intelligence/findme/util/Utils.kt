package com.intelligence.findme.util

import android.util.Log
import android.view.View

class Utils {

    companion object {
        const val ALL_SERVICES_TOKEN = "8JPqEv%qaU60g&KKTaotK"
        const val NEARBY_CONTRACTORS_TOKEN = "p*7ns6iVM%z&0FD9wTpxH"
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

    }
}