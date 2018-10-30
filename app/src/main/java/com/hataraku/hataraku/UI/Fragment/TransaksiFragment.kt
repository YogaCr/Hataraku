package com.hataraku.hataraku.UI.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_transaksi.*
import org.json.JSONObject

class TransaksiFragment : Fragment() {
    lateinit var pref: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaksi, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        val dialog = SpotsDialog.Builder()
                .setContext(context!!)
                .setMessage("Harap Tunggu")
                .setCancelable(false)
                .build()
                .apply {
                    show()
                }
        AndroidNetworking.get(ApiEndPoint.TAWARAN.value + "?id_lowongan=" + activity?.intent?.getIntExtra("id", 1) + "&status=3")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        val obj = response.getJSONArray("data").getJSONObject(0)
                        tv_tukang_nama.text = obj.getString("nama")
                        tv_tukang_hp.text = obj.getString("no_hp")
                        tv_tarif.text = obj.getString("tarif")
                        tgl_selesai.text = obj.getString("tgl_selesai")
                        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value + "/" + activity?.intent?.getIntExtra("id", 1))
                                .addHeaders("Content-Type", "application/json")
                                .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
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
