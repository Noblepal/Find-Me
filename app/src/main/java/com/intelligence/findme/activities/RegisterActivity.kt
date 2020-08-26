package com.intelligence.findme.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.intelligence.findme.R
import com.intelligence.findme.models.RegisterResponse
import com.intelligence.findme.retrofit.RetrofitClient
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
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        context = this

        fabRegister.setOnClickListener {

        }

    }

    private fun prepareData() {
        showView(registerProgress)
        hideView(fabRegister)

        val username = edtRegUsername.text.toString().trim()
        val email = edtRegEmail.text.toString().trim()
        val phone = edtPhone.text.toString().trim()
        val password = edtRegPassword.text.toString().trim()
        val passwordC = edtConfPassword.text.toString().trim()

        if (TextUtils.isEmpty(username)) {
            edtRegUsername.error = "Username is required"
            edtRegUsername.requestFocus()
        } else if (TextUtils.isEmpty(email)) {
            edtRegEmail.error = "Email is required"
            edtRegEmail.requestFocus()
        } else if (TextUtils.isEmpty(phone)) {
            edtPhone.error = "Phone is required"
            edtPhone.requestFocus()
        } else if (TextUtils.isEmpty(password)) {
            edtRegPassword.error = "Password is required"
            edtRegPassword.requestFocus()
        } else if (TextUtils.isEmpty(passwordC)) {
            edtConfPassword.error = "Password confirmation is required"
            edtConfPassword.requestFocus()
        } else if (password != passwordC) {
            edtConfPassword.error = "Passwords do not match"
            edtConfPassword.requestFocus()
        }

        RetrofitClient.instance.registerUser(
            username, email, phone, password
        ).enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                logThis(0, "onFailure", "Error: ${t.localizedMessage}")
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                hideView(registerProgress)
                showView(fabRegister)

                if (response.isSuccessful) {
                    if (!response.body()!!.error) {

                        logThis(0, "OnResponse", "Successfully registered")
                        startActivity(Intent(applicationContext, MainActivity::class.java))

                    } else {
                        Toast.makeText(
                            context,
                            "Failed to register",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Unsuccessful Failed to register",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        })
    }
}
