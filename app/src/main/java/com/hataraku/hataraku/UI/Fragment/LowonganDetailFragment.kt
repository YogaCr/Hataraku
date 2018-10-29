package com.hataraku.hataraku.UI.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.Model.TawaranModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Adapter.TawaranAdapter
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import dmax.dialog.SpotsDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_lowongan_detail.*
import org.json.JSONObject
import java.math.BigDecimal

class LowonganDetailFragment : Fragment() {
    lateinit var pref: SharedPreferences
    val tawaran: MutableList<TawaranModel> = mutableListOf()

    lateinit var dialog: AlertDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_lowongan_detail, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_tawaran.adapter = TawaranAdapter(tawaran, context!!)
        rv_tawaran.layoutManager = LinearLayoutManager(context!!)
        activity?.findViewById<FloatingActionButton>(R.id.fab_tawaran)?.visibility = View.VISIBLE
        activity?.title = "Detail Lowongan"
        dialog = SpotsDialog.Builder()
                .setContext(context!!)
                .setMessage("Harap Tunggu")
                .setCancelable(false)
                .build()
                .apply {
                    show()
                }

        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)

        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value + "/" + activity?.intent?.getIntExtra("id", 1))
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        tv_nama.text = response?.getString("nama")
                        tv_alamat.text = response?.getString("alamat")
                        tv_batas.text = response?.getString("tgl_akhir")
                        tv_budget.text = "Rp. " + response?.getString("budget")
                        tv_isi.text = response?.getString("isi")
                        tv_judul.text = response?.getString("judul")
                        tv_skill.text = response?.getString("skill")
                        activity?.findViewById<FloatingActionButton>(R.id.fab_tawaran)?.setOnClickListener {
                            dialog.show()
                            AndroidNetworking.get(ApiEndPoint.TAWARAN.value + "?id_user=" + pref.getInt(Preferences.ID_USER.name, 0))
                                    .addHeaders("Content-Type", "application/json")
                                    .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                                    .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                                    .build()
                                    .getAsJSONObject(object : JSONObjectRequestListener {
                                        override fun onResponse(response: JSONObject) {
                                            val arr = response.getJSONArray("data")
                                            if (arr.length() > 0) {
                                                Toasty.error(context!!, "Anda sudah memberikan tawaran sebelumnya", Toast.LENGTH_SHORT, true).show()
                                            } else {
                                                val Bundle = Bundle()
                                                Bundle.putInt("id", activity?.intent?.getIntExtra("id", 0)!!)
                                                Bundle.putInt("id_kategori", response.getInt("id_kategori"))
                                                it.visibility = View.INVISIBLE
                                                Navigation.findNavController(view).navigate(R.id.action_lowonganDetailFragment_to_tawaranFragment, Bundle)
                                            }
                                            dialog.dismiss()
                                        }

                                        override fun onError(anError: ANError?) {

                                        }

                                    })

                        }

                        initTawaran()
                    }

                    override fun onError(anError: ANError?) {
                        dialog.dismiss()
                        Toasty.error(context!!, JSONObject(anError?.errorBody).getString("message"), Toast.LENGTH_SHORT).show()
                    }

                })
    }

    fun initTawaran() {
        AndroidNetworking.get(ApiEndPoint.TAWARAN.value + "?id_lowongan=" + activity?.intent?.getIntExtra("id", 1))
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        tawaran.clear()
                        val arr = response.getJSONArray("data")
                        var x = 0
                        while (x < arr.length()) {
                            val obj = arr.getJSONObject(x)
                            val twr = TawaranModel(obj.getString("proposal"),
                                    BigDecimal.valueOf(obj.getDouble("tarif")).toPlainString(),
                                    obj.getString("tgl_selesai"),
                                    obj.getInt("id_user"),
                                    obj.getString("rating"),
                                    obj.getString("nama"),
                                    obj.getInt("id"),
                                    obj.getString("foto"))
                            tawaran.add(twr)
                            x = x.inc()
                        }


                        rv_tawaran.adapter?.notifyDataSetChanged()
                        dialog.dismiss()
                    }

                    override fun onError(anError: ANError?) {
                        dialog.dismiss()
                        Toasty.error(context!!, JSONObject(anError?.errorBody).getString("message"), Toast.LENGTH_SHORT).show()
                    }

                })
    }
}
