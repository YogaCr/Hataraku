package com.hataraku.hataraku.UI.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hataraku.hataraku.R
import com.hataraku.hataraku.Utilities.Preferences

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val pref = getSharedPreferences(Preferences.HatarakuPreferences.name, Context.MODE_PRIVATE)
        if (pref.getBoolean(Preferences.IS_LOGIN.name, false)) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
}
