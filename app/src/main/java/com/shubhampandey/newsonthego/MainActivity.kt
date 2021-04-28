package com.shubhampandey.newsonthego

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.view.menu.MenuBuilder
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        setupNavigationDrawer()
    }

    /**
     * Setup the Drawer
     */
    private fun setupNavigationDrawer() {
        // Setup the Navigation Drawer
        appBarConfiguration = AppBarConfiguration(
            // Add fragments in which you want
            // to show the Drawer icon else up arrow
            // will show except in the mentioned fragment
            setOf(
                R.id.liveNewsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(
            findNavController(R.id.nav_host_fragment),
            appBarConfiguration
        )
        // Attach it with Nav Controller
        // So that which drawer menu we click
        // on it will open that
        navigationView.setupWithNavController(navController)
    }

    //open drawer when drawer icon clicked and back button pressed
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}