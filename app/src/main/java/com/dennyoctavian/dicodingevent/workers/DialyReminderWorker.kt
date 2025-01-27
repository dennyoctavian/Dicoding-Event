package com.dennyoctavian.dicodingevent.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dennyoctavian.dicodingevent.helpers.Helper
import com.dennyoctavian.dicodingevent.models.Event
import com.dennyoctavian.dicodingevent.repositories.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DailyReminderWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            val event = getNearestActiveEvent()
            if (event != null) {
                sendNotification(event)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun getNearestActiveEvent(): Event? {
        return withContext(Dispatchers.IO) {
            val eventRepository = EventRepository()
            val eventData = eventRepository.fetchActiveEvent(1)
            if (eventData.isSuccessful) {
                if ((eventData.body()?.listEvents?.size ?: 0) == 0) {
                    return@withContext null
                }
                Log.v("EVENT", eventData.body()?.listEvents.toString())
                return@withContext eventData.body()?.listEvents?.get(0)
            }
            return@withContext null

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(event: Event) {
        Helper.sendNotification(
            applicationContext,
            "Daily Reminder",
            "Upcoming Event: ${event.name}",
            "Waktu Acara: ${Helper.formatDate(event.beginTime)}"
        )
    }
}
