package com.hataraku.hataraku.UI.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hataraku.hataraku.Model.TawaranModel
import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.tawaran_layout.view.*

class TawaranAdapter(val items: MutableList<TawaranModel>, val context: Context) :
        RecyclerView.Adapter<TawaranAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tawaran: TawaranModel = items.get(position)
        holder.iv_image.setImageResource(R.drawable.listrik)
        holder.tv_tglSelesai.text = "Tanggal selesai : " + tawaran.tgl_selesai
        holder.tv_tukang.text = "Tukang : " + tawaran.nama_tukang
        holder.tv_rating.text = tawaran.rating
        holder.tv_tarif.text = "Rp. " + tawaran.tarif
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TawaranAdapter.ViewHolder {
        return TawaranAdapter.ViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.tawaran_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_proposal = view.txtProposal
        val tv_tglSelesai = view.txtTglSelesai
        val tv_tukang = view.txtOlehTawaran
        val tv_rating = view.txtRating
        val tv_tarif = view.txtTarif
        val iv_image = view.iv_image
    }

}