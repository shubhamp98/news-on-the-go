package com.shubhampandey.livenewsonthego.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.shubhampandey.livenewsonthego.R
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
     * Setup the Navigation Drawer
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