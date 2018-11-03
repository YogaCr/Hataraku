package com.hataraku.hataraku.UI.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.MainActivity
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_rating.*
import org.json.JSONObject


class RatingActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        pref = getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        AndroidNetworking.get(ApiEndPoint.USER.value + "/" + pref.getInt(Preferences.LAST_TUKANG.name, 0))
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        tv_nama.text = response.getString("nama")
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
        btn_simpan.setOnClickListener {
            if (smile_rating.rating == 0) {
                Toasty.warning(this, "Tolong beri penilaian terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                AndroidNetworking.post(ApiEndPoint.REVIEW.value)
                        .addHeaders("Content-Type", "application/json")
                        .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                        .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                        .addBodyParameter("id_user", pref.getInt(Preferences.ID_USER.name, 0).toString())
                        .addBodyParameter("id_tukang", pref.getInt(Preferences.LAST_TUKANG.name, 0).toString())
                        .addBodyParameter("keterangan", "tes")
                        .addBodyParameter("nilai", smile_rating.rating.toString())
                        .build().getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
                                Toasty.info(this@RatingActivity, "Terima kasih atas masukan anda", Toast.LENGTH_SHORT).show()
                                val editor = pref.edit()
                                editor.putBoolean(Preferences.SUDAH_RATING.name, true)
                                editor.apply()
                                val intent = Intent(this@RatingActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            override fun onError(anError: ANError?) {

                            }
                        })
            }
        }
    }
}
