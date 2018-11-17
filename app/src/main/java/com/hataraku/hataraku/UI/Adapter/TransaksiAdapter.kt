package com.hataraku.hataraku.UI.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hataraku.hataraku.Model.TransaksiModel
import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.transaksi_layout.view.*


class TransaksiAdapter(val items: MutableList<TransaksiModel>, val context: Context?) :
        RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.transaksi_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TransaksiAdapter.ViewHolder, position: Int) {
        val transaksi: TransaksiModel = items.get(position)
        holder.tv_judul.text = transaksi.judul
        holder.tv_kategori.text = transaksi.kategori
        when (transaksi.id_kategori) {
            1 -> holder.iv_kategori.setImageResource(R.drawable.bata)
            2 -> holder.iv_kategori.setImageResource(R.drawable.palu)
            3 -> holder.iv_kategori.setImageResource(R.drawable.kuas)
            4 -> holder.iv_kategori.setImageResource(R.drawable.listrik)
            5 -> holder.iv_kategori.setImageResource(R.drawable.air)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val tv_judul = view.txtJudulTrans
        val tv_kategori = view.txtKetTrans
        val iv_kategori = view.iv_Trans
    }
}
