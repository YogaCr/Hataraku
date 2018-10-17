package com.hataraku.hataraku.UI.Activity

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.hataraku.hataraku.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val navController = findNavController(this, R.id.nav_host)
        navController.addOnNavigatedListener { _, destination ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }
        }
    }
}
