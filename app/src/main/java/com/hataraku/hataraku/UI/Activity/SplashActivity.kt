package com.hataraku.hataraku.UI.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hataraku.hataraku.R
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        thread(start = true) {
            try {
                // Do any other initialization here
                Thread.sleep(3000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
    }
}
