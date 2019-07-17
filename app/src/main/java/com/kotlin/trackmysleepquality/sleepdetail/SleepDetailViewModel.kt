package com.kotlin.trackmysleepquality.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.trackmysleepquality.database.SleepDatabaseDao
import com.kotlin.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.Job

class SleepDetailViewModel(
        sleepNightKey: Long = 0L,
        dao: SleepDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()

    private val night: LiveData<SleepNight> = dao.getNightWithId(sleepNightKey)

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun getNight() = night

    fun onClose() {
        _navigateToSleepTracker.value = true
    }

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}