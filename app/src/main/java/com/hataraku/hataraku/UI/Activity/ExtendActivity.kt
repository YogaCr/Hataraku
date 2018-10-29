package com.hataraku.hataraku.UI.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hataraku.hataraku.R

class ExtendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//        actionBar?.setDisplayShowHomeEnabled(true)
//        actionBar?.setHomeButtonEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.home ){
            finish()
        }
        return true
    }
}
