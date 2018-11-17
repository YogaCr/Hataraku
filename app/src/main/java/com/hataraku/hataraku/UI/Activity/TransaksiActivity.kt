package com.hataraku.hataraku.UI.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Fragment.RatingActivity
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import dmax.dialog.SpotsDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_transaksi.*
import org.json.JSONObject

class TransaksiActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)
        val context = this
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
                        val id_tukang = obj.getInt("id_user")
                        val id_tawaran = obj.getInt("id")
                        tv_tukang_nama.text = obj.getString("nama")
                        tv_tukang_hp.text = "+62" + obj.getString("no_hp")
                        tv_tarif.text = "Rp." + obj.getString("tarif")
                        tgl_selesai.text = obj.getString("tgl_selesai")

                        btn_selesai.setOnClickListener {
                            val confirm = MaterialStyledDialog.Builder(context).setTitle("Konfirmasi")
                                    .setDescription("Apakah anda yakin pekerjaan sudah selesai?")
                                    .setIcon(R.drawable.ic_info_outline_white_48dp)
                                    .setStyle(Style.HEADER_WITH_ICON)
                                    .setHeaderColor(R.color.colorPrimary)
                                    .setPositiveText("Iya")
                                    .setNegativeText("Tidak")
                                    .onPositive { _, _ ->
                                        AndroidNetworking.put(ApiEndPoint.TRANSAKSI.value + "/" + id_tawaran)
                                                .addHeaders("Content-Type", "application/json")
                                                .addHeaders("X-API-Key", resources?.getString(R.string.x_api_key))
                                                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                                                .addJSONObjectBody(JSONObject().put("status", 2).put("id_tukang", id_tukang))
                                                .build()
                                                .getAsJSONObject(object : JSONObjectRequestListener {
                                                    override fun onResponse(response: JSONObject?) {
                                                        Toasty.success(context, "Sukses", Toast.LENGTH_SHORT, true).show()
                                                        val editor = pref.edit()
                                                        editor.putBoolean(Preferences.SUDAH_RATING.name, false)
                                                        editor.putInt(Preferences.LAST_TUKANG.name, id_tukang)
                                                        editor.apply()
                                                        val intent = Intent(context, RatingActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }

                                                    override fun onError(anError: ANError?) {
                                                        Log.d("Transaksi error", anError!!.errorBody)
                                                    }
                                                })
                                    }
                                    .build()
                            confirm.show()
                        }
                        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value + "/" + intent?.getIntExtra("id", 1))
                                .addHeaders("Content-Type", "application/json")
                                .addHeaders("X-API-Key", resources?.getString(R.string.x_api_key))
                                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener {
                                    override fun onResponse(ob: JSONObject) {
                                        tv_nama_client.text = ob.getString("nama")
                                        tv_client_hp.text = "+62" + ob.getString("no_hp")
                                        tv_judul.text = ob.getString("judul")
                                        tv_isi.text = ob.getString("isi")
                                        tv_kategori.text = ob.getString("nama_kategori")
                                        tv_skill.text = ob.getString("skill")
                                        tv_alamat.text = ob.getString("alamat")
                                        if (pref.getInt(Preferences.ID_USER.name, 0) != ob.getInt("id_user") || ob.getInt("status") != 3) {
                                            btn_selesai.visibility = View.INVISIBLE
                                        } else {
                                            btn_selesai.visibility = View.VISIBLE
                                        }
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
