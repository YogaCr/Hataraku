package com.hataraku.hataraku.UI.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import com.hataraku.hataraku.UI.Activity.ExtendProfileActivity
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
    var status = 1
    val tawaran: MutableList<TawaranModel> = mutableListOf()

    lateinit var dialog: AlertDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lowongan_detail, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        rv_tawaran.layoutManager = LinearLayoutManager(context!!)
        if (pref.getBoolean(Preferences.IS_TUKANG.name, false)) {
            activity?.findViewById<FloatingActionButton>(R.id.fab_tawaran)?.visibility = View.VISIBLE
        } else {
            activity?.findViewById<FloatingActionButton>(R.id.fab_tawaran)?.visibility = View.INVISIBLE
        }
        activity?.title = "Detail Lowongan"
        dialog = SpotsDialog.Builder()
                .setContext(context!!)
                .setMessage("Harap Tunggu")
                .setCancelable(false)
                .build()
                .apply {
                    show()
                }
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
                        tv_budget.text = "Rp. " + BigDecimal.valueOf(response!!.getDouble("budget")).toPlainString()
                        tv_isi.text = response.getString("isi")
                        tv_judul.text = response.getString("judul")
                        tv_skill.text = response.getString("skill")
                        tv_kategori.text = response.getString("nama_kategori")
                        status = response.getInt("status")
                        val kategori = response.getInt("id_kategori")
                        when (kategori) {
                            1 -> iv_kategori.setImageResource(R.drawable.bata)
                            2 -> iv_kategori.setImageResource(R.drawable.palu)
                            3 -> iv_kategori.setImageResource(R.drawable.kuas)
                            4 -> iv_kategori.setImageResource(R.drawable.listrik)
                            5 -> iv_kategori.setImageResource(R.drawable.air)
                        }
                        if (response.getInt("id_user") == pref.getInt(Preferences.ID_USER.name, 0)) {
                            rv_tawaran.adapter = TawaranAdapter(tawaran, context!!, true)
                            activity?.findViewById<FloatingActionButton>(R.id.fab_tawaran)?.visibility = View.INVISIBLE
                        } else {
                            rv_tawaran.adapter = TawaranAdapter(tawaran, context!!, false)
                        }
                        if (status != 2) {
                            activity?.findViewById<FloatingActionButton>(R.id.fab_tawaran)?.setOnClickListener {
                                dialog.show()
                                AndroidNetworking.get(ApiEndPoint.TAWARAN.value + "?id_user=" + pref.getInt(Preferences.ID_USER.name, 0) + "&id_lowongan=" + activity?.intent?.getIntExtra("id", 1))
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
                                                    if (pref.getString(Preferences.ABOUT.name, "").equals("") ||
                                                            pref.getString(Preferences.ALAMAT.name, "").equals("") ||
                                                            pref.getString(Preferences.NO_HP.name, "").equals("") ||
                                                            pref.getString(Preferences.KELAMIN.name, "").equals("") ||
                                                            pref.getString(Preferences.TGL_LAHIR.name, "").equals("") ||
                                                            pref.getString(Preferences.NAMA.name, "").equals("")) {
                                                        Toasty.info(context!!, "Anda harus melengkapi profil anda terlebih dahulu").show()
                                                        val intent = Intent(context!!, ExtendProfileActivity::class.java)
                                                        startActivity(intent)
                                                    } else {
                                                        val Bundle = Bundle()
                                                        Bundle.putInt("id", activity?.intent?.getIntExtra("id", 0)!!)
                                                        Bundle.putInt("id_kategori", kategori)
                                                        it.visibility = View.INVISIBLE
                                                        Navigation.findNavController(view).navigate(R.id.action_lowonganDetailFragment_to_tawaranFragment, Bundle)
                                                    }
                                                }
                                                dialog.dismiss()
                                            }

                                            override fun onError(anError: ANError?) {

                                            }

                                        })

                            }
                            initTawaran()
                        } else {
                            tv_done.visibility = View.VISIBLE
                            ly_tawaran.visibility = View.INVISIBLE
                            dialog.dismiss()
                        }
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
                        if (arr.length() > 0) {
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

                        } else {
                            rv_tawaran.visibility = View.INVISIBLE
                            tv_tawaran_none.visibility = View.VISIBLE
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
