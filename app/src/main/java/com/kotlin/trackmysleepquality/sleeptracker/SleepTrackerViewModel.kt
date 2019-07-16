package com.kotlin.trackmysleepquality.sleeptracker

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    val nightsString: LiveData<Spanned> = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    private var tonight = MutableLiveData<SleepNight?>()

    init {
        initializeTonight()
    }

    // SOS: I must specify the returned type explicitly for these calls, because Transformations.map
    // is a Java call that does not specify whether it returns Nullable or NotNull, so I get a warning
    // (remove type to see it). IOW, here I must decide if it's Boolean or Boolean?
    val startButtonVisible: LiveData<Boolean> = Transformations.map(tonight) {
        it == null
    }

    val stopButtonVisible: LiveData<Boolean> = Transformations.map(tonight) {
        it != null
    }

    val clearButtonVisible: LiveData<Boolean> = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    // SOS: We want the fragment to handle navigation, so we'll make it observe a LiveData that we
    // change when the navigation must be done
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()

    // SOS: the fragment actually observes this val which hides the above val (so that the fragment
    // can't change the value of the LiveData..
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    private var _showSnackBarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = dao.getTonight()
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
            val lastNight = tonight.value ?: return@launch
            lastNight.endTimeMilli = System.currentTimeMillis()
            updateDb(lastNight)
            _navigateToSleepQuality.value = lastNight
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
            _showSnackBarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            dao.clear()
        }
    }

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

