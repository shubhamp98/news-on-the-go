package com.shubhampandey.newsonthego

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.view.menu.MenuBuilder
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setClickListener()
    }

    /**
     * Attach the listener on Navigation items
     */
    private fun setClickListener() {
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_menu -> navigateToHomeDestination()
                R.id.category_menu -> navigateToCategoryDestination()
                R.id.bookmarked_menu -> navigateToBookmarkedDestination()

            }
            drawerLayout.closeDrawers()
            // returning true will make only currently selected menu item as checked
            true
        }
    }

    private fun navigateToHomeDestination() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.liveNewsFragment)
    }

    private fun navigateToBookmarkedDestination() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.bookmarkedNewsFragment)
    }

    private fun navigateToCategoryDestination() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.newsCategoryFragment)
    }

    /**
     * Setup the UI elements
     * Support the custom actionbar/toolbar
     */
    private fun setupUI() {
        setSupportActionBar(toolbar_main)

        // It ties DrawerLayout with Toolbar/ActionBar.
        drawerToggle =
            ActionBarDrawerToggle(
                this, drawerLayout, toolbar_main,
                R.string.open, R.string.close
            )

        // Change drawer arrow color
        drawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }
}