package com.hataraku.hataraku.UI.Fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.hataraku.hataraku.Model.LowonganModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Adapter.LowonganAdapter
import kotlinx.android.synthetic.main.fragment_cari.*

class CariFragment : Fragment() {

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
        addLowongan()
        rv_lowongan.layoutManager = LinearLayoutManager(context)
        rv_lowongan.adapter = LowonganAdapter(lowongan, context)
        activity?.title = "Cari Lowongan"
    }

    private fun addLowongan() {
        var x = 10
        val lowongan_dummy: LowonganModel = LowonganModel(
                "Pembangunan Gazebo", "Bangunan", "22-09-2018",
                1000000.0, "Yoga Aranggi"
        )
        while (x > 0) {
            lowongan.add(lowongan_dummy)
            x--
        }
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
                    Log.i("onQueryTextSubmit", query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    Log.i("onQueryTextChange", newText)
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
