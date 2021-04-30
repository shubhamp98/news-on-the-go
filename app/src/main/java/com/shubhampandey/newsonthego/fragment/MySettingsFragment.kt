package com.shubhampandey.newsonthego.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.shubhampandey.newsonthego.R

class MySettingsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}