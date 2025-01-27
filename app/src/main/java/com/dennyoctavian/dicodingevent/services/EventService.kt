package com.dennyoctavian.dicodingevent.services

import com.dennyoctavian.dicodingevent.models.DetailEventAPI
import com.dennyoctavian.dicodingevent.models.ListEventAPI
import com.dennyoctavian.dicodingevent.utils.Helper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET(Helper.EVENT_URL)
    suspend fun getListEvents(
        @Query("active") active: Int?,
        @Query("q") keyword: String?,
        @Query("limit") limit: Int?
    ): Response<ListEventAPI>

    @GET("${Helper.EVENT_URL}/{id}")
    suspend fun getDetailEvent(@Path("id") id: Long): Response<DetailEventAPI>
}