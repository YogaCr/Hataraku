package com.hataraku.hataraku.UI.Fragment

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
import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import es.dmoral.toasty.Toasty
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
            if (et_email_lupa.validEmail().not()) {
                et_email_lupa.error = "Tolong masukkan email dengan benar"
                return@setOnClickListener
            }
            AndroidNetworking.post(ApiEndPoint.AUTH_FORGOTPASS.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                    .addBodyParameter("email", et_email_lupa.text.toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Toasty.success(context!!, "Kode berhasil dikirim. Silahkan cek email anda", Toast.LENGTH_SHORT, true).show()
                        }

                        override fun onError(anError: ANError?) {
                            if (anError?.errorCode != 200) {
                                Log.d("message", anError?.errorBody)
                                Toasty.error(context!!, JSONObject(anError?.errorBody?.toString()).getString("message"), Toast.LENGTH_SHORT, true).show()
                            }
                        }
                    })
        }
        btn_toResetPassword.setOnClickListener {
            findNavController(it).navigate(R.id.action_lupaPasswordFragment_to_resetPasseordFragment)
        }
    }
}
