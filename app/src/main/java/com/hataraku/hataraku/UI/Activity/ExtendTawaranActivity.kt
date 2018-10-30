package com.hataraku.hataraku.UI.Activity

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hataraku.hataraku.R

class ExtendTawaranActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extend_tawaran)
    }
}
