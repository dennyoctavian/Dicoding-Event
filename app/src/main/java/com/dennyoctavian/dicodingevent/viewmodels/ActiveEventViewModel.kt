package com.dennyoctavian.dicodingevent.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennyoctavian.dicodingevent.models.Event
import com.dennyoctavian.dicodingevent.repositories.EventRepository
import kotlinx.coroutines.launch

class ActiveEventViewModel : ViewModel() {
    private val event = EventRepository()
    private val _activeEvent = MutableLiveData<List<Event>>()
    val activeEvent: LiveData<List<Event>> get() = _activeEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadActiveEvents() {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                val activeEvents = event.fetchActiveEvent(null)
                if (activeEvents.isSuccessful) {
                    _activeEvent.postValue(activeEvents.body()?.listEvents)
                    _error.postValue(null)
                } else {
                    _error.postValue(activeEvents.message())
                }
            }catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                e.printStackTrace()
                _error.postValue("Something went wrong")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

}
