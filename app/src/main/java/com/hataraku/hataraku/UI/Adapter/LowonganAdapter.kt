package com.hataraku.hataraku.UI.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hataraku.hataraku.Model.LowonganModel
import com.hataraku.hataraku.R
import com.hataraku.hataraku.UI.Activity.ExtendTawaranActivity
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.lowongan_layout.view.*

class LowonganAdapter(val items: ArrayList<LowonganModel>, val context: Context?) :
        RecyclerView.Adapter<LowonganAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.lowongan_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: LowonganAdapter.ViewHolder, position: Int) {
        val lowongan: LowonganModel = items.get(position)
        holder.tv_judul.text = lowongan.judul
        holder.tv_kategori.text = lowongan.kategori
        holder.tv_tgl.text = "Batas :  " + lowongan.tgl_akhir
        holder.tv_oleh.text = "Oleh : " + lowongan.oleh
        holder.tv_budget.text = "Rp. " + lowongan.budget
        holder.ly_lowongan.setOnClickListener {
            val intent = Intent(context!!, ExtendTawaranActivity::class.java)
            intent.putExtra("id", lowongan.id)
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val tv_judul = view.txtJudul
        val tv_kategori = view.txtKet
        val tv_tgl = view.txtTgl
        val tv_budget = view.txtBudget
        val tv_oleh = view.txtOleh
        val iv_profile = view.iv_profile
        val ly_lowongan = view.ly_lowongan
    }
}