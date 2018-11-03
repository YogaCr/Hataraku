package com.hataraku.hataraku.UI.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_portfolio.*
import org.json.JSONObject

class PortfolioFragment : Fragment() {

    lateinit var pref: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        btn_simpan.setOnClickListener {
            AndroidNetworking.post(ApiEndPoint.PORTOFOLIO.value)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("X-API-Key", activity?.resources?.getString(R.string.x_api_key))
                    .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                    .addBodyParameter("id_user", pref.getInt(Preferences.ID_USER.name, 0).toString())
                    .addBodyParameter("id_kategori", arguments!!.getInt("id_kategori").toString())
                    .addBodyParameter("judul", et_judul.text.toString())
                    .addBodyParameter("isi", et_keterangan.text.toString())
                    .addBodyParameter("file", et_portfolio.text.toString())
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Toasty.success(context!!, "Sukses menambahkan portfolio", Toast.LENGTH_SHORT, true).show()
                            Navigation.findNavController(view).navigate(R.id.action_portfolioFragment_to_tawaranFragment)
                        }

                        override fun onError(anError: ANError?) {
                            Log.d("Portfolio error", anError!!.errorBody)
                        }
                    })
        }
    }
}
