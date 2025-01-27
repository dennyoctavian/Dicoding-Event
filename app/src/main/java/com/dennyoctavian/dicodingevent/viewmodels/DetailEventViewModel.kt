package com.dennyoctavian.dicodingevent.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennyoctavian.dicodingevent.models.Event
import com.dennyoctavian.dicodingevent.repositories.EventRepository
import kotlinx.coroutines.launch

class DetailEventViewModel : ViewModel() {
    private val event = EventRepository()
    private val _detailEvent = MutableLiveData<Event>()
    val detailEvent: LiveData<Event> get() = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadAPastEvents(id: Long) {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                val detailEvents = event.fetchDetailEvent(id)
                if (detailEvents.isSuccessful) {
                    _detailEvent.postValue(detailEvents.body()?.event)
                    _error.postValue(null)
                } else {
                    _error.postValue(detailEvents.message())
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
