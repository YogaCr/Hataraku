package com.hataraku.hataraku.UI.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.TransaksiActivity
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import dmax.dialog.SpotsDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_tawaran_next.*
import org.json.JSONObject
import java.math.BigDecimal

class TawaranNextFragment : Fragment() {
    lateinit var pref: SharedPreferences
    var id_tukang = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tawaran_next, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.findViewById<FloatingActionButton>(R.id.fab_tawaran).visibility = View.INVISIBLE
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)

        val dialog = SpotsDialog.Builder()
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
                    override fun onResponse(response: JSONObject) {
                        tv_kategori.text = response.getString("nama_kategori")
                        tv_skill.text = response.getString("skill")

                        AndroidNetworking.get(ApiEndPoint.TAWARAN.value + "/" + arguments!!.getString("id_tawaran"))
                                .addHeaders("Content-Type", "application/json")
                                .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener {
                                    override fun onResponse(response: JSONObject) {
                                        val total = BigDecimal.valueOf(response.getDouble("tarif")).toPlainString()
                                        tv_proposal.text = response.getString("proposal")
                                        tv_selesai.text = response.getString("tgl_selesai")
                                        tv_tarif.text = "Rp. " + total
                                        tv_tukang.text = response.getString("nama")
                                        tv_rating.text = response.getString("rating")
                                        id_tukang = response.getInt("id_user")

                                        val confirm = MaterialStyledDialog.Builder(context!!).setTitle("Konfirmasi")
                                                .setDescription("Apakah anda yakin ingin menggunakan jasa tukang ini?")
                                                .setIcon(R.drawable.ic_info_outline_white_48dp)
                                                .setStyle(Style.HEADER_WITH_ICON)
                                                .setHeaderColor(R.color.colorPrimary)
                                                .setPositiveText("Iya")
                                                .setNegativeText("Tidak")
                                                .onPositive { _, _ ->
                                                    AndroidNetworking.put(ApiEndPoint.TAWARAN.value + "/status/" + arguments!!.getString("id_tawaran"))
                                                            .addHeaders("Content-Type", "application/json")
                                                            .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                                                            .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                                                            .addJSONObjectBody(JSONObject().put("status", 3).put("id_tukang", id_tukang))
                                                            .build()
                                                            .getAsJSONObject(object : JSONObjectRequestListener {
                                                                override fun onResponse(response: JSONObject) {
                                                                    AndroidNetworking.post(ApiEndPoint.TRANSAKSI.value)
                                                                            .addHeaders("Content-Type", "application/json")
                                                                            .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                                                                            .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                                                                            .addBodyParameter("id_tawaran", arguments!!.getString("id_tawaran"))
                                                                            .addBodyParameter("total", total)
                                                                            .addBodyParameter("catatan", "")
                                                                            .build()
                                                                            .getAsJSONObject(object : JSONObjectRequestListener {
                                                                                override fun onResponse(response: JSONObject?) {
                                                                                    Toasty.success(context!!, "Sukses", Toast.LENGTH_SHORT, true).show()
                                                                                    val intent = Intent(context!!, TransaksiActivity::class.java)
                                                                                    intent.putExtra("id", activity?.intent?.getIntExtra("id", 1))
                                                                                    startActivity(intent)
                                                                                    activity!!.finish()
                                                                                }

                                                                                override fun onError(anError: ANError?) {

                                                                                }
                                                                            })

                                                                }

                                                                override fun onError(anError: ANError?) {

                                                                }
                                                            })
                                                }
                                                .build()

                                        btn_transaksi.setOnClickListener {
                                            confirm.show()
                                        }

                                        dialog.dismiss()
                                    }

                                    override fun onError(anError: ANError?) {
                                        dialog.dismiss()
                                        Toasty.error(context!!, JSONObject(anError?.errorBody).getString("message"), Toast.LENGTH_SHORT).show()
                                    }
                                })
                    }

                    override fun onError(anError: ANError?) {
                        dialog.dismiss()
                        Toasty.error(context!!, JSONObject(anError?.errorBody).getString("message"), Toast.LENGTH_SHORT).show()
                    }
                })
        /* btn_pesan.setOnClickListener {
             val waIntent = Intent()
             waIntent.action = Intent.ACTION_SEND

         }*/
    }
}
