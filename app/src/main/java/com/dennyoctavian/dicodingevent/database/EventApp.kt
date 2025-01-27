package com.dennyoctavian.dicodingevent.database

import android.app.Application

class EventApp : Application() {
    val db by lazy {
        EventDatabase.getDatabase(this)
    }
}