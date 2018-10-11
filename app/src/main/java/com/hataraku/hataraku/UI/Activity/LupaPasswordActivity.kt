package com.hataraku.hataraku.UI.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.activity_lupa_password.*
import org.json.JSONObject

class LupaPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_password)
        btn_lupa_password.setOnClickListener {
            AndroidNetworking.post("")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", "8JDWKFC6AWZ2019LULUSUNBCWAWQCK56")
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
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}

