package com.dennyoctavian.dicodingevent.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dennyoctavian.dicodingevent.R
import com.dennyoctavian.dicodingevent.workers.DailyReminderWorker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

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

    fun sendNotification(context: Context, channelId: String, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelName = "Dicoding Event"
            val channelDescription = "Channel for app notifications from Dicoding Event"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = channelDescription
                }
                notificationManager.createNotificationChannel(channel)
            }
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSubText("Reminder")
            .build()

        notificationManager.notify(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(indonesiaTime: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(indonesiaTime, formatter)

        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", Locale("id", "ID"))
        return localDateTime.format(outputFormatter)
    }

    fun scheduleDailyReminder(context: Context) {
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context).enqueue(dailyWorkRequest)
    }
}