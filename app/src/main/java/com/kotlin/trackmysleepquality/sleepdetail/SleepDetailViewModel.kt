package com.kotlin.trackmysleepquality.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.trackmysleepquality.database.SleepDatabaseDao

class SleepDetailViewModel(sleepNightKey: Long = 0L,
                           dao: SleepDatabaseDao) : ViewModel() {

    val night = dao.getNightWithId(sleepNightKey)

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun onClose() {
        _navigateToSleepTracker.value = true
    }

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }
}