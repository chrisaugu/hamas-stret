package io.fantastix.hamasstret.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import io.fantastix.hamasstret.R

class AppPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private val s = PreferenceManager.setDefaultValues(context, R.xml.preferences, false)

    // String preference
    var username: String?
        get() = sharedPreferences.getString(KEY_USERNAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_USERNAME, value).apply()

    // Boolean preference
    var isDarkMode: Boolean
        get() = sharedPreferences.getBoolean(KEY_DARK_MODE, false)
        set(value) = sharedPreferences.edit { putBoolean(KEY_DARK_MODE, value) }

    // Integer preference
    var notificationCount: Int
        get() = sharedPreferences.getInt(KEY_NOTIFICATION_COUNT, 0)
        set(value) = sharedPreferences.edit { putInt(KEY_NOTIFICATION_COUNT, value) }

    // Clear preferences
    fun clear() {
        sharedPreferences.edit { clear() }
    }

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATION_COUNT = "notification_count"
    }
}