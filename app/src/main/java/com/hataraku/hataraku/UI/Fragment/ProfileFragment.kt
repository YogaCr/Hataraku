package com.hataraku.hataraku.UI.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hataraku.hataraku.Model.TransaksiModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Adapter.TransaksiAdapter
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    val transaksi: ArrayList<TransaksiModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Profil"
        addTransaksi()
        rv_transaksi.layoutManager = LinearLayoutManager(context)
        rv_transaksi.adapter = TransaksiAdapter(transaksi, context)
    }

    private fun addTransaksi() {
        var x = 10
        val trans: TransaksiModel = TransaksiModel(
                "Pembangunan Rumah", "Bangunan", "22-09-2018",
                1000000.0, "Yoga Aranggi"
        )
        while (x > 0){
            transaksi.add(trans)
            x--
        }
    }

}
