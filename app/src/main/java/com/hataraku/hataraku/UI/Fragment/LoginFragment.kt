package com.hataraku.hataraku.UI.Fragment

import Utilities.Preferences
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject

class LoginFragment : Fragment() {
    lateinit var pref: SharedPreferences
    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Toast.makeText(context, Preferences.HatarakuPreferences.name, Toast.LENGTH_SHORT).show()
        pref = context!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)

        /*if (pref.getBoolean(Preferences.IS_LOGIN.name, false)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }*/

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(context!!, gso)
        inisialisasiButton()
    }

    private fun inisialisasiButton() {
        btn_lupa_password.setOnClickListener {
            findNavController(it).navigate(R.id.action_loginFragment_to_lupaPasswordFragment)
        }
        btn_toRegister.setOnClickListener {
            findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        btn_login.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
            AndroidNetworking.post("")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", "8JDWKFC6AWZ2019LULUSUNBCWAWQCK56")
                    .addBodyParameter("email", et_email_login.text.toString())
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

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}
