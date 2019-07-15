package com.kotlin.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.kotlin.trackmysleepquality.database.SleepDatabaseDao
import com.kotlin.trackmysleepquality.database.SleepNight
import com.kotlin.trackmysleepquality.formatNights
import kotlinx.coroutines.*

class SleepTrackerViewModel(
        private val dao: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val nights = dao.getAllNights()

    // SOS: aha. So transformation is applied whenever nights change and it too returns LiveData
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    private var tonight = MutableLiveData<SleepNight?>()

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = dao.getTonight()
            // SOS: if these are different, that means this night is a past (completed) night
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insertDb(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insertDb(night: SleepNight) {
        withContext(Dispatchers.IO) {
            dao.insert(night)
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            // SOS: nice! Didn't know I can return to specific function
            val lastNight = tonight.value ?: return@launch
            lastNight.endTimeMilli = System.currentTimeMillis()
            updateDb(lastNight)
        }
    }

    private suspend fun updateDb(night: SleepNight) {
        withContext(Dispatchers.IO) {
            dao.update(night)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            dao.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

