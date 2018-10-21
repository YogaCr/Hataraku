package com.hataraku.hataraku.Model

data class TransaksiModel(val judul: String, val kategori: String,
                         var tgl_selesai: String, var tarif: Double, var oleh: String)