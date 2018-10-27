package com.hataraku.hataraku.UI.Fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.MainActivity
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import es.dmoral.toasty.Toasty
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
            if (et_email_reset.validEmail().not()) {
                et_email_reset.error = "Tolong masukkan email dengan benar"
                return@setOnClickListener
            }
            if (!et_new_pass_reset.text.toString().equals(et_new_pass_confirm_reset.text.toString())) {
                et_new_pass_confirm_reset.error = "Password tidak cocok"
                return@setOnClickListener
            }

            btn_reset_password.isEnabled = false

            AndroidNetworking.post(ApiEndPoint.AUTH_RESETPASS.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                    .addBodyParameter("email", et_email_reset.text.toString())
                    .addBodyParameter("code", et_kode_reset.text.toString())
                    .addBodyParameter("password", et_new_pass_reset.text.toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Navigation.findNavController(it).navigateUp()
                        }

                        override fun onError(anError: ANError?) {
                            if (anError?.errorCode != 200)
                                Toasty.error(context!!, JSONObject(anError?.errorBody?.toString()).getString("message"), Toast.LENGTH_SHORT, true).show()
                            btn_reset_password.isEnabled = true
                        }
                    })
        }
    }
}
