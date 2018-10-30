package com.hataraku.hataraku.UI.Activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_transaksi.*
import org.json.JSONObject

class TransaksiActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)
        pref = getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        val dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Harap Tunggu")
                .setCancelable(false)
                .build()
                .apply {
                    show()
                }
        AndroidNetworking.get(ApiEndPoint.TAWARAN.value + "?id_lowongan=" + intent?.getIntExtra("id", 1) + "&status=3")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", resources?.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        val obj = response.getJSONArray("data").getJSONObject(0)
                        tv_tukang_nama.text = obj.getString("nama")
                        tv_tukang_hp.text = obj.getString("no_hp")
                        tv_tarif.text = obj.getString("tarif")
                        tgl_selesai.text = obj.getString("tgl_selesai")
                        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value + "/" + intent?.getIntExtra("id", 1))
                                .addHeaders("Content-Type", "application/json")
                                .addHeaders("X-API-Key", resources?.getString(R.string.x_api_key))
                                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener {
                                    override fun onResponse(ob: JSONObject) {
                                        tv_nama_client.text = ob.getString("nama")
                                        tv_client_hp.text = ob.getString("no_hp")
                                        tv_judul.text = ob.getString("judul")
                                        tv_isi.text = ob.getString("isi")
                                        tv_kategori.text = ob.getString("nama_kategori")
                                        tv_skill.text = ob.getString("skill")
                                        tv_alamat.text = ob.getString("alamat")
                                        dialog.dismiss()
                                    }

                                    override fun onError(anError: ANError?) {

                                    }
                                })
                    }

                    override fun onError(anError: ANError?) {

                    }

                })
    }
}
