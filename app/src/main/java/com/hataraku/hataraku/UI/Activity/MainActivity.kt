package com.hataraku.hataraku.UI.Activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.hataraku.hataraku.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.nav_main)
        val navBottom = findViewById<BottomNavigationView>(R.id.nav_bottom)
        setupBottomNavMenu(navController)

    }

    private fun setupBottomNavMenu(navController: NavController) {
        nav_bottom?.let { bottomNavView ->
            NavigationUI.setupWithNavController(bottomNavView, navController)
        }
    }

}
