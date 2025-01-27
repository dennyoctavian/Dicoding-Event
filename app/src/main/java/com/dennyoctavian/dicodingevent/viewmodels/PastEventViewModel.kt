package com.dennyoctavian.dicodingevent.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennyoctavian.dicodingevent.models.Event
import com.dennyoctavian.dicodingevent.repositories.EventRepository
import kotlinx.coroutines.launch

class PastEventViewModel : ViewModel() {
    private val event = EventRepository()
    private val _pastEvent = MutableLiveData<List<Event>>()
    val pastEvent: LiveData<List<Event>> get() = _pastEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadAPastEvents() {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                val pastEvents = event.fetchPastEvent(null)
                if (pastEvents.isSuccessful) {
                    _pastEvent.postValue(pastEvents.body()?.listEvents)
                    _error.postValue(null)
                } else {
                    _error.postValue(pastEvents.message())
                }
            }catch (e: Exception) {
                e.printStackTrace()
                _error.postValue("Something went wrong")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun loadSearchEvents(keyword: String) {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                val pastEvents = event.fetchSearchEvent(keyword)
                if (pastEvents.isSuccessful) {
                    _pastEvent.postValue(pastEvents.body()?.listEvents)
                    _error.postValue(null)
                } else {
                    _error.postValue(pastEvents.message())
                }
            }catch (e: Exception) {
                e.printStackTrace()
                _error.postValue("Something went wrong")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
