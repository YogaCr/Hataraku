package com.hataraku.hataraku.UI.Fragment

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hataraku.hataraku.Model.LowonganModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Adapter.LowonganAdapter
import com.hataraku.hataraku.Utilities.ApiEndPoint
import com.hataraku.hataraku.Utilities.Preferences
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_cari.*
import org.json.JSONObject

class CariFragment : Fragment() {
    private lateinit var pref: SharedPreferences
    private var searchView: SearchView? = null
    lateinit var queryTextListener: SearchView.OnQueryTextListener
    lateinit var searchItem: MenuItem
    lateinit var searchManager: SearchManager
    val lowongan: ArrayList<LowonganModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cari, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = activity!!.getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        if (pref.getBoolean(Preferences.IS_TUKANG.name, false)) {
            ly_bukan_tukang.visibility = View.INVISIBLE
            rv_lowongan.visibility = View.VISIBLE
        } else {
            ly_bukan_tukang.visibility = View.VISIBLE
            rv_lowongan.visibility = View.INVISIBLE
        }
        rv_lowongan.layoutManager = LinearLayoutManager(context)
        rv_lowongan.adapter = LowonganAdapter(lowongan, context)
        addLowongan()
        activity?.title = "Cari Lowongan"
    }

    private fun addLowongan() {
        lowongan.clear()
        rv_lowongan.adapter?.notifyDataSetChanged()
        AndroidNetworking.cancelAll()
        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val arr = response?.getJSONArray("data")
                        var x = 0
                        while (x < arr!!.length()) {
                            val obj = arr.getJSONObject(x)
                            val l = LowonganModel(obj.getInt("id"), obj.getString("judul"), obj.getString("nama_kategori"), obj.getString("tgl_akhir"), obj.getString("budget"), obj.getString("nama"))
                            lowongan.add(l)
                            x = x.inc()
                        }
                        rv_lowongan.adapter?.notifyDataSetChanged()
                    }

                    override fun onError(anError: ANError?) {
                        Toasty.error(context!!, anError!!.message!!, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_menu, menu)
        searchItem = menu?.findItem(R.id.action_search) as MenuItem
        searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = searchItem.actionView as SearchView

        if (searchView != null) {
            searchView?.setSearchableInfo(
                    searchManager.getSearchableInfo(activity?.componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.equals("")) {
                        addLowongan()
                    } else {
                        lowongan.clear()
                        rv_lowongan.adapter?.notifyDataSetChanged()
                        AndroidNetworking.cancelAll()
                        AndroidNetworking.get(ApiEndPoint.LOWONGAN.value + "?search=" + newText)
                                .addHeaders("Content-Type", "application/json")
                                .addHeaders("X-API-Key", resources.getString(R.string.x_api_key))
                                .addHeaders("Authorization", "Bearer " + pref.getString(Preferences.API_KEY.name, ""))
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener {
                                    override fun onResponse(response: JSONObject?) {
                                        val arr = response?.getJSONArray("data")
                                        var x = 0
                                        while (x < arr!!.length()) {
                                            val obj = arr.getJSONObject(x)
                                            val l = LowonganModel(obj.getInt("id"), obj.getString("judul"), obj.getString("nama_kategori"), obj.getString("tgl_akhir"), obj.getString("budget"), obj.getString("nama"))
                                            lowongan.add(l)
                                            x = x.inc()
                                        }
                                        rv_lowongan.adapter?.notifyDataSetChanged()
                                    }

                                    override fun onError(anError: ANError?) {
                                        Toasty.error(context!!, anError!!.message!!, Toast.LENGTH_SHORT).show()
                                    }
                                })
                    }
                    return true
                }
            }
            searchView?.setOnQueryTextListener(queryTextListener)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search ->
                // Not implemented here
                return false
            else -> {
            }
        }
        searchView?.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

}
