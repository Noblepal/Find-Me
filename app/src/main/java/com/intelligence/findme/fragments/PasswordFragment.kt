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
import com.intelligence.findme.util.Utils
import com.intelligence.findme.util.Utils.hideView
import com.intelligence.findme.util.Utils.md5
import com.intelligence.findme.util.Utils.showView


class PasswordFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_password, container, false)
        val edtPassword = v.findViewById<TextInputEditText>(R.id.edt_password)
        val tvErrorMessage = v.findViewById<TextView>(R.id.tvPasswordErrorMessage)
        var isFaulty: Boolean
        edtPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 6) {
                    showView(tvErrorMessage)
                    isFaulty = true
                } else {
                    hideView(tvErrorMessage)
                    isFaulty = false

                    Handler().postDelayed({
                        SharedPrefsManager.getInstance(context!!)
                            ?.setTempPass(md5(s.toString().trim()))
                    }, 1000)

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
        fun newInstance() = PasswordFragment()
    }
}
