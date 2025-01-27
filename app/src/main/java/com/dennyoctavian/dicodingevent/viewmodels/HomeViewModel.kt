package com.dennyoctavian.dicodingevent.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennyoctavian.dicodingevent.models.Event
import com.dennyoctavian.dicodingevent.repositories.EventRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val event = EventRepository()

    private val _activeEvent = MutableLiveData<List<Event>>()
    val activeEvent: LiveData<List<Event>> get() = _activeEvent

    private val _pastEvent = MutableLiveData<List<Event>>()
    val pastEvent: LiveData<List<Event>> get() = _pastEvent

    private val _isLoadingActiveEvent = MutableLiveData<Boolean>()
    val isLoadingActiveEvent: LiveData<Boolean> get() = _isLoadingActiveEvent

    private val _isLoadingPastEvent = MutableLiveData<Boolean>()
    val isLoadingPastEvent: LiveData<Boolean> get() = _isLoadingPastEvent

    private val _errorActiveEvent = MutableLiveData<String?>()
    val errorActiveEvent: LiveData<String?> get() = _errorActiveEvent

    private val _errorPastEvent = MutableLiveData<String?>()
    val errorPastEvent: LiveData<String?> get() = _errorPastEvent

    fun loadActiveEvents() {
        viewModelScope.launch {
            try {
                _isLoadingActiveEvent.postValue(true)
                val activeEvents = event.fetchActiveEvent(null)
                if (activeEvents.isSuccessful) {
                    _activeEvent.postValue(activeEvents.body()?.listEvents)
                    _errorActiveEvent.postValue(null)
                } else {
                    _errorActiveEvent.postValue(activeEvents.message())
                }
            }catch (e: Exception) {
                e.printStackTrace()
                _errorActiveEvent.postValue("Something went wrong")
            } finally {
                _isLoadingActiveEvent.postValue(false)
            }
        }
    }

    fun loadAPastEvents() {
        viewModelScope.launch {
            try {
                _isLoadingPastEvent.postValue(true)
                val pastEvents = event.fetchPastEvent(5)
                if (pastEvents.isSuccessful) {
                    _pastEvent.postValue(pastEvents.body()?.listEvents)
                    _errorPastEvent.postValue(null)
                } else {
                    _errorPastEvent.postValue(pastEvents.message())
                }
            }catch (e: Exception) {
                e.printStackTrace()
                _errorPastEvent.postValue("Something went wrong")
            } finally {
                _isLoadingPastEvent.postValue(false)
            }
        }
    }
}
