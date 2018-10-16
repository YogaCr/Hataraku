package com.hataraku.hataraku.UI.Fragment

import Utilities.ApiEndPoint
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
import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.fragment_lupa_password.*
import org.json.JSONObject

class LupaPasswordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lupa_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_lupa_password.setOnClickListener {
            AndroidNetworking.post(ApiEndPoint.AUTH_FORGOTPASS.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", "8JDWKFC6AWZ2019LULUSUNBCWAWQCK56")
                    .addBodyParameter("email", et_email_lupa.text.toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {

                        }

                        override fun onError(anError: ANError?) {

                        }
                    })
        }
        btn_toResetPassword.setOnClickListener {
            findNavController(it).navigate(R.id.action_lupaPasswordFragment_to_resetPasseordFragment)
        }
    }
}
