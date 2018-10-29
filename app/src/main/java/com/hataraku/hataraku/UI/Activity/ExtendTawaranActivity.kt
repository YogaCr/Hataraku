package com.hataraku.hataraku.UI.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.navigation.Navigation.findNavController
import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.activity_extend_tawaran.*

class ExtendTawaranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extend_tawaran)

        fab_tawaran.setOnClickListener {
            findNavController(it).navigate(R.id.action_lowonganDetailFragment_to_tawaranFragment)
        }
    }
}
