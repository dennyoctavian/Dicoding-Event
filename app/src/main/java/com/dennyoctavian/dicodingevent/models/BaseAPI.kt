package com.dennyoctavian.dicodingevent.models

data class ListEventAPI(
    val error: Boolean,
    val message: String,
    val listEvents: List<Event>,
)

data class DetailEventAPI(
    val error: Boolean,
    val message: String,
    val event: Event,
)