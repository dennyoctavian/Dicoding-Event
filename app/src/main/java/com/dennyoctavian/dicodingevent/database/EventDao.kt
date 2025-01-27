package com.dennyoctavian.dicodingevent.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dennyoctavian.dicodingevent.models.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert
    suspend fun insert(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM `events`")
    fun getAllEventsSaved(): Flow<List<Event>>

    @Query("SELECT * FROM `events` WHERE id = :id")
    suspend fun getEventById(id: String): Event?
}