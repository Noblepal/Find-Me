package com.intelligence.findme.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences

    companion object {
        private const val shared_prefs_name: String = "find_me"
        private var mInstance: SharedPrefsManager? = null

        @Synchronized
        fun getInstance(context: Context): SharedPrefsManager? {
            if (mInstance == null) {
                mInstance = SharedPrefsManager(context)
            }
            return mInstance
        }
    }

    fun setOneOrMoreFragmentFault(fragmentName: String, isFaulty: Boolean) {
        if (isFaulty) sharedPreferences.edit().putString("faulty", fragmentName).apply()
    }

    fun setTempUsername(s: String) {
        sharedPreferences.edit()
            .putString("temp_username", s)
            .apply()
    }

    fun setTempPhone(s: String) {
        sharedPreferences.edit()
            .putString("temp_phone", s)
            .apply()
    }

    fun setTempPass(s: String) {
        sharedPreferences.edit()
            .putString("temp_key", s)
            .apply()
    }

    fun getTempData(): Array<String> {
        return arrayOf(
            sharedPreferences.getString("temp_username", null)!!,
            sharedPreferences.getString("temp_phone", null)!!,
            sharedPreferences.getString("temp_key", null)!!
        )
    }


    //private Context mCtx;
    init {
        sharedPreferences = context.getSharedPreferences(
            shared_prefs_name,
            Context.MODE_PRIVATE
        )
    }
}