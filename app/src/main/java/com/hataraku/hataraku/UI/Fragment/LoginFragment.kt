package com.hataraku.hataraku.UI.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.wajahatkarim3.easyvalidation.core.view_ktx.minLength
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import es.dmoral.toasty.Toasty
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
            if (et_email_login.validEmail().not()) {
                et_email_login.error = "Tolong masukkan email dengan benar"
                return@setOnClickListener
            }
            if (et_pass_login.nonEmpty().not().and(et_pass_login.minLength(8).not())) {
                et_pass_login.error = "Tolong masukkan password dengan benar"
                return@setOnClickListener
            }

            ly_login.visibility = View.INVISIBLE
            pb_login.visibility = View.VISIBLE

            AndroidNetworking.post(ApiEndPoint.AUTH_LOGIN.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                    .addBodyParameter("email", et_email_login.text.toString())
                    .addBodyParameter("password", et_pass_login.text.toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            val pref = context!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putString(Preferences.EMAIL.name, et_email_login.text.toString())
                            editor.putString(Preferences.API_KEY.name, response.getString("api_token"))
                            editor.putString(Preferences.NAMA.name, response.getJSONObject("user").getString("nama"))
                            editor.putBoolean(Preferences.IS_LOGIN.name, true)
                            editor.apply()
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }

                        override fun onError(anError: ANError?) {
                            if (anError?.errorCode != 200) {
                                Log.d("message", anError?.errorBody)
                                Toasty.error(context!!, JSONObject(anError?.errorBody?.toString()).getString("message"), Toast.LENGTH_SHORT, true).show()
                            }
                            ly_login.visibility = View.VISIBLE
                            pb_login.visibility = View.INVISIBLE
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

                                val edit = pref.edit()
                                edit.putBoolean(Preferences.IS_LOGIN.name, true)
                                edit.putString(Preferences.NAMA.name, account.displayName)
                                edit.putString(Preferences.EMAIL.name, account.email)
                                edit.putString(Preferences.API_KEY.name, response?.getString("api_token"))
                                edit.apply()
                                googleSignInClient.signOut()

                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }

                            override fun onError(anError: ANError?) {

                            }
                        })


            }
        }
    }
}
