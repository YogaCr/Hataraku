package com.hataraku.hataraku.UI.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
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
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.wajahatkarim3.easyvalidation.core.view_ktx.maxLength
import com.wajahatkarim3.easyvalidation.core.view_ktx.minLength
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import es.dmoral.toasty.Toasty
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
            if (et_email_register.validEmail().not()) {
                et_email_register.error = "Tolong masukkan email dengan benar"
                return@setOnClickListener
            }
            if (et_password_register.nonEmpty().not().and(et_password_register.minLength(6).not()).and(et_password_register.maxLength(12))) {
                et_password_register.error = "Tolong masukkan password dengan benar"
                return@setOnClickListener
            }
            if (!et_password_register.text.toString().equals(et_passconfirm_register.text.toString())) {
                et_passconfirm_register.error = "Pastikan password sudah benar"
                return@setOnClickListener
            }
            ly_register.visibility = View.INVISIBLE
            pb_register.visibility = View.VISIBLE

//            Log.d("url", ApiEndPoint.AUTH_REGISTER.value)
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
                            Toasty.success(context!!, "Sukses mendaftar", Toast.LENGTH_SHORT, true).show()
                            Navigation.findNavController(view).navigateUp()
                        }

                        override fun onError(anError: ANError?) {
                            if (anError?.errorCode != 200) {
                                Log.d("message", anError?.errorBody)
                                Toasty.error(context!!, JSONObject(anError?.errorBody?.toString()).getString("message"), Toast.LENGTH_SHORT, true).show()
                            }
                            ly_register.visibility = View.VISIBLE
                            pb_register.visibility = View.INVISIBLE
                        }
                    })
        }
    }
}
