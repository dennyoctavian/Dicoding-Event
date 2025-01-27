package com.dennyoctavian.dicodingevent.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
@Parcelize
data class Event(
    @PrimaryKey
    val id: Long,
    val name: String,
    val summary: String,
    val description: String,
    val imageLogo: String,
    val mediaCover: String,
    val category: String,
    val ownerName: String,
    val cityName: String,
    val quota: Long,
    val registrants: Long,
    val beginTime: String,
    val endTime: String,
    val link: String,
): Parcelable


