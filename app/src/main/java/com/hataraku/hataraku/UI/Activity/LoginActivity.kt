package com.hataraku.hataraku.UI.Activity

import Utilities.Preferences
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient

    val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Toast.makeText(this, Preferences.HatarakuPreferences.name, Toast.LENGTH_SHORT).show()
        pref = getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)

        /*if (pref.getBoolean(Preferences.IS_LOGIN.name, false)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }*/

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        inisialisasiButton()
    }

    private fun inisialisasiButton() {
        btn_lupa_password.setOnClickListener {
            val intent = Intent(this, LupaPasswordActivity::class.java)
            startActivity(intent)
        }
        btn_toRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btn_login.setOnClickListener {
            AndroidNetworking.post("")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", "8JDWKFC6AWZ2019LULUSUNBCWAWQCK56")
                    .addBodyParameter("nama", et_email_login.text.toString())
                    .addBodyParameter("password", et_pass_login.text.toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {

                        }

                        override fun onError(anError: ANError?) {

                        }
                    })
        }
        btn_login_google.setOnClickListener {
            signin()
        }
    }

    fun signin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            AndroidNetworking.post("")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", "8JDWKFC6AWZ2019LULUSUNBCWAWQCK56")
                    .addBodyParameter("nama", account.displayName)
                    .addBodyParameter("email", account.email)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {

                        }

                        override fun onError(anError: ANError?) {

                        }
                    })
            googleSignInClient.signOut()

            /*val edit = pref.edit()
            edit.putBoolean(Preferences.IS_LOGIN.name, true)
            edit.apply()*/

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}
