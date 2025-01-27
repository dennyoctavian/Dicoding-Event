package com.dennyoctavian.dicodingevent.repositories

import com.dennyoctavian.dicodingevent.models.DetailEventAPI
import com.dennyoctavian.dicodingevent.models.ListEventAPI
import com.dennyoctavian.dicodingevent.services.APIClient
import com.dennyoctavian.dicodingevent.services.EventService
import retrofit2.Response

class EventRepository {
    private val eventService = APIClient.instance.create(EventService::class.java)

   suspend fun fetchActiveEvent(limit: Int?): Response<ListEventAPI> {
       return eventService.getListEvents(1, null, limit)
   }

    suspend fun fetchPastEvent(limit: Int?): Response<ListEventAPI> {
        return eventService.getListEvents(0, null,limit)
    }

    suspend fun fetchSearchEvent(keyword: String): Response<ListEventAPI> {
        return eventService.getListEvents(-1, keyword, null)
    }

    suspend fun fetchDetailEvent( id: Long): Response<DetailEventAPI> {
        return eventService.getDetailEvent(id)

    }
}