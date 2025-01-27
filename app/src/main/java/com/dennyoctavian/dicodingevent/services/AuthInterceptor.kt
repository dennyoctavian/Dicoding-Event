package com.dennyoctavian.dicodingevent.services

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val authenticatedRequest = request.newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()

        val response = chain.proceed(authenticatedRequest)
        Log.v("API", response.toString())
        return response
    }
}
