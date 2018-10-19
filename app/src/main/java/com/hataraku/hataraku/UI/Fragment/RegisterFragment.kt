package com.hataraku.hataraku.UI.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.MainActivity
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import kotlinx.android.synthetic.main.fragment_register.*
import org.json.JSONObject

class RegisterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_toLogin.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        btn_register.setOnClickListener {
            Log.d("url", ApiEndPoint.AUTH_REGISTER.value)
            AndroidNetworking.post(ApiEndPoint.AUTH_REGISTER.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                    .addBodyParameter("nama", et_nama_register.text.toString())
                    .addBodyParameter("email", et_email_register.text.toString())
                    .addBodyParameter("password", et_password_register.text.toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            val pref = context!!.getSharedPreferences(Preferences.NAMA.name, Context.MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putString(Preferences.EMAIL.name, et_email_register.text.toString())
                            editor.putString(Preferences.API_KEY.name, response?.getString("api_token"))
                            editor.apply()
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }

                        override fun onError(anError: ANError?) {
                            Log.d("Network Error", anError?.message)
                        }
                    })
        }
    }
}
