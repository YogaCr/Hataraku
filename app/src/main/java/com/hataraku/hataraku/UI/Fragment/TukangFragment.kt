package com.hataraku.hataraku.UI.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.Model.PortfolioModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.MainActivity
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import kotlinx.android.synthetic.main.fragment_lowongan.*
import kotlinx.android.synthetic.main.fragment_tukang.*
import org.json.JSONObject


class TukangFragment : Fragment() {
    val listPortofolio: MutableList<PortfolioModel> = mutableListOf()
    lateinit var pref: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tukang, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Daftar Tukang"
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        btn_simpan.setOnClickListener {
            AndroidNetworking.post(ApiEndPoint.HANDYMAN.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
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
                            val intent = Intent(context!!, MainActivity::class.java)
                        }

                        override fun onError(anError: ANError?) {

                        }
                    })
        }
    }


    /*fun initPortfolio() {
        val context = context
        val dialog = SpotsDialog.Builder()
                .setContext(context!!)
                .setMessage("Harap Tunggu")
                .setCancelable(false)
                .build()
                .apply {
                    show()
                }
//        Toasty.info(context, pref.getInt(Preferences.ID_USER.name, 0).toString(), Toast.LENGTH_SHORT).show()
        AndroidNetworking.get(ApiEndPoint.PORTOFOLIO.value + "?id_user=" + pref.getInt(Preferences.ID_USER.name, 0) + "&id_kategori=" + arguments?.getInt("id_kategori"))
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        var x = 0
                        while (x < response.length()) {
                            val obj = response.getJSONObject(x)
                            val p = PortfolioModel(obj.getInt("id"), obj.getString("judul"), obj.getString("isi"), obj.getString("kategori"))
                            listPortofolio.add(p)
                            x = x.inc()
                        }
                        spin_portofolio.adapter = CustomAdapter(context, R.layout.spinner_layout, listPortofolio)
                        dialog.dismiss()
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }*/
}
