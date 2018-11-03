package com.hataraku.hataraku.UI.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.MainActivity
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_daftar_tukang.*
import org.json.JSONObject

class DaftarTukangActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_tukang)
        title = "Daftar Tukang"
        pref = getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        btn_simpan.setOnClickListener {
            AndroidNetworking.post(ApiEndPoint.HANDYMAN.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", resources?.getString(R.string.x_api_key))
                    .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                    .addBodyParameter("id_user", pref.getInt(Preferences.ID_USER.name, 0).toString())
                    .addBodyParameter("id_kategori", spin_kategori.selectedItemPosition.toString())
                    .addBodyParameter("skill", et_skill.text.toString())
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            val edit = pref.edit()
                            edit.putBoolean(Preferences.IS_TUKANG.name, true)
                            edit.apply()
                            Toasty.info(this@DaftarTukangActivity, "Berhasil mendaftar sebagai tukang").show()
                            val intent = Intent(this@DaftarTukangActivity, MainActivity::class.java)
                        }

                        override fun onError(anError: ANError?) {

                        }
                    })
        }
    }
}
