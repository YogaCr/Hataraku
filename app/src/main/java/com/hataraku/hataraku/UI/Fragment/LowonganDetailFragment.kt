package com.hataraku.hataraku.UI.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.Model.TawaranModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import dmax.dialog.SpotsDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_extend_tawaran.*
import kotlinx.android.synthetic.main.fragment_lowongan_detail.*
import org.json.JSONObject

class LowonganDetailFragment : Fragment() {
    lateinit var pref: SharedPreferences
    val tawaran: MutableList<TawaranModel> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_lowongan_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Detail Lowongan"
        val dialog = SpotsDialog.Builder()
                .setContext(context!!)
                .setMessage("Harap Tunggu")
                .setCancelable(false)
                .build()
                .apply {
                    show()
                }

        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        val i = activity?.intent
        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value + "/" + i?.getIntExtra("id", 1))
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
                        dialog.dismiss()
                    }

                    override fun onError(anError: ANError?) {
                        dialog.dismiss()
                        Toasty.error(context!!, JSONObject(anError?.errorBody).getString("message"), Toast.LENGTH_SHORT).show()
                    }

                })
    }
}
