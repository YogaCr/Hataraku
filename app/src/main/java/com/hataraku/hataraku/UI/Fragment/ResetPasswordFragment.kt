package com.hataraku.hataraku.UI.Fragment

import Utilities.ApiEndPoint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener

import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.MainActivity
import kotlinx.android.synthetic.main.fragment_reset_password.*
import org.json.JSONObject

class ResetPasswordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_reset_password.setOnClickListener {
            if (et_new_pass_reset.text.toString().equals(et_new_pass_confirm_reset.text.toString())) {
                AndroidNetworking.post(ApiEndPoint.AUTH_RESETPASS.value)
                        .addHeaders("Content-Type", "application/json")
                        .addHeaders("X-API-Key", "8JDWKFC6AWZ2019LULUSUNBCWAWQCK56")
                        .addBodyParameter("email", et_email_reset.text.toString())
                        .addBodyParameter("code", et_kode_reset.text.toString())
                        .addBodyParameter("password", et_new_pass_reset.text.toString())
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
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
