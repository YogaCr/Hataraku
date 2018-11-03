package com.hataraku.hataraku.UI.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.Model.LowonganModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.ExtendProfileActivity
import com.hataraku.hataraku.UI.Adapter.LowonganAdapter
import com.hataraku.hataraku.UI.Adapter.SlideAdapter
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_project.*
import org.json.JSONObject
import java.util.*

class ProjectFragment : Fragment() {

    internal lateinit var viewPager: ViewPager
    internal lateinit var indicator: TabLayout
    lateinit var pref: SharedPreferences
    var currentPage = 0
    val lowonganList: MutableList<LowonganModel> = mutableListOf()
    val proyekList: MutableList<LowonganModel> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun onResume() {
        super.onResume()
        initLowonganSaya()
        initProyekSaya()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Proyek"
        pref = context!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        if (pref.getBoolean(Preferences.IS_TUKANG.name, false)) {
            ly_daftar_tukang.visibility = View.GONE
            cv_proyek.visibility = View.VISIBLE
            rv_proyek_saya.layoutManager = LinearLayoutManager(context!!)
            rv_proyek_saya.adapter = LowonganAdapter(proyekList, context!!)
        } else {
            ly_daftar_tukang.visibility = View.VISIBLE
            cv_proyek.visibility = View.GONE
        }
        ly_daftar_tukang.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_projectFragment_to_daftarTukangActivity)
        }
        rv_lowongan_saya.layoutManager = LinearLayoutManager(context!!)
        rv_lowongan_saya.adapter = LowonganAdapter(lowonganList, context!!)
        viewPager = view.findViewById(R.id.slider_pager) as ViewPager
        indicator = view.findViewById(R.id.indicator) as TabLayout

        initProyek()

        val adapter = SlideAdapter(context)
        viewPager.adapter = adapter
        indicator.setupWithViewPager(viewPager, true)


        val handler = Handler()
        val Update = Runnable {
            if (currentPage == adapter.count) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)
    }

    private fun initProyek() {
        var bundle = Bundle()
        proyek_bangun.setOnClickListener {
            bundle.putInt("kategori", 1)
            navigateProyek(it, R.id.action_projectFragment_to_extendActivity, bundle)
        }
        proyek_renov.setOnClickListener {
            bundle.putInt("kategori", 2)
            navigateProyek(it, R.id.action_projectFragment_to_extendActivity, bundle)
        }
        proyek_cat.setOnClickListener {
            bundle.putInt("kategori", 3)
            navigateProyek(it, R.id.action_projectFragment_to_extendActivity, bundle)
        }
        proyek_ledeng.setOnClickListener {
            bundle.putInt("kategori", 5)
            navigateProyek(it, R.id.action_projectFragment_to_extendActivity, bundle)
        }
        proyek_listrik.setOnClickListener {
            bundle.putInt("kategori", 4)
            navigateProyek(it, R.id.action_projectFragment_to_extendActivity, bundle)
        }
    }

    fun navigateProyek(v: View, dest: Int, bundle: Bundle) {
        if (pref.getBoolean(Preferences.SUDAH_RATING.name, true)) {
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
                AndroidNetworking.cancelAll()
                findNavController(v).navigate(dest, bundle)
            }
        } else {
            val intent = Intent(context!!, RatingActivity::class.java)
            startActivity(intent)
        }
    }

    fun initProyekSaya() {
        proyekList.clear()
        AndroidNetworking.get(ApiEndPoint.TAWARAN.value + "?id_user=" + pref.getInt(Preferences.ID_USER.name, 0) + "&status=3")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val arr = response?.getJSONArray("data")
                        var x = 0
                        while (x < arr!!.length()) {
                            val obj = arr.getJSONObject(x)
                            getLowongan(obj.getInt("id_lowongan"))
                            x = x.inc()
                        }
                        if (arr.length() == 0) {
                            rv_proyek_saya.visibility = View.INVISIBLE
                            tv_proyek_none.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Toasty.error(context!!, anError!!.message!!, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    fun initLowonganSaya() {
        lowonganList.clear()
        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value + "?id_user=" + pref.getInt(Preferences.ID_USER.name, 0))
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val arr = response?.getJSONArray("data")
                        var x = 0
                        while (x < arr!!.length()) {
                            val obj = arr.getJSONObject(x)
                            val l = LowonganModel(obj.getInt("id"), obj.getString("judul"), obj.getString("nama_kategori"), obj.getString("tgl_akhir"), obj.getString("budget"), obj.getString("nama"), obj.getInt("status"))
                            lowonganList.add(l)
                            x = x.inc()
                        }
                        if (lowonganList.size > 0) {
                            rv_lowongan_saya.visibility = View.VISIBLE
                            tv_lowongan_none.visibility = View.INVISIBLE
                        } else {
                            rv_lowongan_saya.visibility = View.INVISIBLE
                            tv_lowongan_none.visibility = View.VISIBLE
                        }
                        rv_lowongan_saya.adapter!!.notifyDataSetChanged()
                    }

                    override fun onError(anError: ANError?) {
                        Toasty.error(context!!, anError!!.message!!, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    fun getLowongan(id_lowongan: Int) {
        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value + "/" + id_lowongan)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(obj: JSONObject) {
                        val l = LowonganModel(obj.getInt("id"), obj.getString("judul"), obj.getString("nama_kategori"), obj.getString("tgl_akhir"), obj.getString("budget"), obj.getString("nama"), obj.getInt("status"))
                        proyekList.add(l)
                        if (proyekList.size > 0) {
                            rv_proyek_saya.visibility = View.VISIBLE
                            tv_proyek_none.visibility = View.INVISIBLE
                        } else {
                            rv_proyek_saya.visibility = View.INVISIBLE
                            tv_proyek_none.visibility = View.VISIBLE
                        }
                        rv_proyek_saya.adapter!!.notifyDataSetChanged()
                    }

                    override fun onError(anError: ANError?) {
                        Toasty.error(context!!, anError!!.message!!, Toast.LENGTH_SHORT).show()
                    }
                })
    }
}
