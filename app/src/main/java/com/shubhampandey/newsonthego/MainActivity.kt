package com.shubhampandey.newsonthego

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.view.menu.MenuBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)

        // It ties DrawerLayout with Toolbar/ActionBar.
        drawerToggle =
            ActionBarDrawerToggle(
                this, drawerLayout, toolbar_main,
                R.string.open, R.string.close
            )
        drawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setOnClickListener {
            when (it.id) {
                R.id.home_menu -> {
                    drawerLayout.closeDrawers()
                }
                R.id.category_menu -> {
                    drawerLayout.closeDrawers()
                }
                R.id.bookmarked_menu -> {
                    drawerLayout.closeDrawers()
                }
                else -> {
                    // TODO Open home
                    drawerLayout.closeDrawers()
                }
            }
        }
    }
}