package com.hataraku.hataraku.UI.Fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener

import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.MainActivity
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_lowongan_next.*
import org.json.JSONObject


class LowonganNextFragment : Fragment() {
    lateinit var pref: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lowongan_next, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        tv_nama.text = pref.getString(Preferences.NAMA.name, "")
        tv_judul.text = arguments!!.getString("judul")
        tv_isi.text = arguments!!.getString("isi")
        tv_kategori.text = arguments!!.getString("nama_kategori")
        tv_alamat.text = arguments!!.getString("alamat")
        tv_skill.text = arguments!!.getString("skill")
        tv_budget.text = "Rp. " + arguments!!.getString("budget")
        tv_batas.text = arguments!!.getString("tgl_akhir")
        val kategori = arguments!!.getInt("kategori")
        when (kategori) {
            1 -> iv_kategori.setImageResource(R.drawable.bata)
            2 -> iv_kategori.setImageResource(R.drawable.palu)
            3 -> iv_kategori.setImageResource(R.drawable.kuas)
            4 -> iv_kategori.setImageResource(R.drawable.listrik)
            5 -> iv_kategori.setImageResource(R.drawable.air)
        }
        btn_simpan_lowongan.setOnClickListener {
            AndroidNetworking.post(ApiEndPoint.LOWONGAN.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                    .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                    .addBodyParameter("id_user", pref.getInt(Preferences.ID_USER.name, 0).toString())
                    .addBodyParameter("id_kategori", arguments!!.getInt("id_kategori").toString())
                    .addBodyParameter("judul", arguments!!.getString("judul"))
                    .addBodyParameter("isi", arguments!!.getString("isi"))
                    .addBodyParameter("alamat", arguments!!.getString("alamat"))
                    .addBodyParameter("budget", arguments!!.getString("budget"))
                    .addBodyParameter("tgl_akhir", arguments!!.getString("tgl_akhir"))
                    .addBodyParameter("lat", "")
                    .addBodyParameter("long", "")
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Toasty.success(context!!, "Sukses menambahkan lowongan", Toast.LENGTH_SHORT, true).show()
                            val intent = Intent(context!!, MainActivity::class.java)
                            startActivity(intent)
                            activity!!.finish()
                        }

                        override fun onError(anError: ANError?) {
                        }
                    })
        }
    }
}
