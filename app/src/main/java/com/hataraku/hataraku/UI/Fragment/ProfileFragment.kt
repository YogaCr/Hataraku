package com.hataraku.hataraku.UI.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.Model.TransaksiModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.AuthActivity
import com.hataraku.hataraku.UI.Activity.ExtendProfileActivity
import com.hataraku.hataraku.UI.Adapter.TransaksiAdapter
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject

class ProfileFragment : Fragment() {

    val transaksi: MutableList<TransaksiModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var pref: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = context!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        cv_Pekerjaan.visibility = View.INVISIBLE
        /*if (!pref.getBoolean(Preferences.IS_TUKANG.name, false)) {
            cv_Pekerjaan.visibility = View.INVISIBLE
        }*/
        activity?.title = "Profil"
        tv_nama.text = pref.getString(Preferences.NAMA.name, "")
        if (!pref.getBoolean(Preferences.IS_TUKANG.name, false)) {
            ly_data_tukang.visibility = View.GONE
        } else {
            getMember()
        }
        tv_email.text = pref.getString(Preferences.EMAIL.name, "")
        if (!pref.getString(Preferences.NO_HP.name, "").equals("")) {
            tv_no_hp.text = "+62" + pref.getString(Preferences.NO_HP.name, "")
        }
        addTransaksi()
        rv_transaksi.layoutManager = LinearLayoutManager(context)
        rv_transaksi.adapter = TransaksiAdapter(transaksi, context)
    }

    private fun addTransaksi() {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuEditProfile -> {
                val intent = Intent(context!!, ExtendProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.menuLogout -> {
                pref.edit().clear().apply()
                val i = Intent(this.context!!, AuthActivity::class.java)
                context!!.startActivity(i)
                activity?.finish()
            }
        }
        return true
    }

    fun getMember() {
        AndroidNetworking.get(ApiEndPoint.MEMBER.value + "/" + pref.getInt(Preferences.ID_USER.name, 0))
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        tv_jml_transaksi.text = response.getInt("jml_transaksi").toString()
                        tv_rating.text = response.getString("rating")
                        if (tv_rating.text == "null") {
                            tv_rating.text = "0"
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Toasty.error(context!!, JSONObject(anError!!.errorBody).getString("message"), Toast.LENGTH_SHORT).show()
                    }

                })
    }
}
