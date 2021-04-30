package com.shubhampandey.newsonthego.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.shubhampandey.newsonthego.R

/**
 * Setting screen
 */
class MySettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}