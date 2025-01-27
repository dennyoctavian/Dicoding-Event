package com.dennyoctavian.dicodingevent.helpers

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object Helper {
    fun setIsDarkMode(context: Context, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences("DicodingEvents", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isDarkMode", value)
        editor.apply()
        AppCompatDelegate.setDefaultNightMode(
            if (value) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun getIsDarkMode(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("DicodingEvents", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isDarkMode", false)
    }

    fun applySavedTheme(context: Context) {
        val sharedPreferences = context.getSharedPreferences("DicodingEvents", Context.MODE_PRIVATE)
        val isDarkModeEnabled = sharedPreferences.getBoolean("isDarkMode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}