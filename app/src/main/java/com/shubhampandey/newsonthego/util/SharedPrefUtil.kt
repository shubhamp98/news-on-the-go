package com.shubhampandey.newsonthego.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.shubhampandey.newsonthego.R

object SharedPrefUtil {

    /**
     * Retrieve selected country from shared pref
     */
    fun getCountryFromPref(activity: Context): String? {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(activity /* Activity context */)
        return sharedPreferences.getString(
            NEWS_BY_COUNTRY_KEY,
            activity.getString(R.string.default_country_india)
        )
    }

    /**
     * Retrieve selected language from shared pref
     */
    fun getLanguageFromPref(activity: Context): String? {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(activity /* Activity context */)
        return sharedPreferences.getString(
            NEWS_LANGUAGE_KEY,
            activity.getString(R.string.default_language_english)
        )
    }

    private const val NEWS_BY_COUNTRY_KEY = "news_by_country"
    private const val NEWS_LANGUAGE_KEY = "news_language"
}