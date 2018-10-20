package com.hataraku.hataraku.UI.Fragment

import android.app.Activity
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
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject

class LoginFragment : Fragment() {
    private lateinit var pref: SharedPreferences
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1

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
            AndroidNetworking.post(ApiEndPoint.AUTH_LOGIN.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                    .addBodyParameter("email", et_email_login.text.toString())
                    .addBodyParameter("password", et_pass_login.text.toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            if (response?.has("message")!!) {

                            } else {
                                val pref = context!!.getSharedPreferences(Preferences.NAMA.name, Context.MODE_PRIVATE)
                                val editor = pref.edit()
                                editor.putString(Preferences.EMAIL.name, et_email_login.text.toString())
                                editor.putString(Preferences.API_KEY.name, response.getString("api_token"))
                                editor.apply()
                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }
                        }

                        override fun onError(anError: ANError?) {

                        }
                    })
        }
        btn_login_google.setOnClickListener {
            signin()
        }
    }

    private fun signin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                AndroidNetworking.post(ApiEndPoint.AUTH_GOOGLE.value)
                        .addHeaders("Content-Type", "application/json")
                        .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
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
}
