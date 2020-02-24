package com.intelligence.findme.fragments

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.intelligence.findme.R
import com.intelligence.findme.util.SharedPrefsManager
import com.intelligence.findme.util.Utils.hideView
import com.intelligence.findme.util.Utils.showView


class UsernameFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_username, container, false)
        val edtUsername = v.findViewById<TextInputEditText>(R.id.edt_username)
        val tvErrorMessage = v.findViewById<TextView>(R.id.tvErrorMessage)
        edtUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 6) {
                    showView(tvErrorMessage)
                } else {
                    hideView(tvErrorMessage)
                    Handler().postDelayed({
                        SharedPrefsManager.getInstance(context!!)
                            ?.setTempUsername(s.toString().trim())
                    }, 1000)
                    //TODO: Handle faulty data
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance() = UsernameFragment()
    }
}
