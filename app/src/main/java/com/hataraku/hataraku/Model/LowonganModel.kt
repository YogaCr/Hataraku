package com.hataraku.hataraku.Model

data class LowonganModel(val id: Int, val judul: String, var kategori: String,
                         var tgl_akhir: String, var budget: String, var oleh: String, val status: Int)