package com.kotlin.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(private val sleepNightKey: Long = 0L,
                            private val dao: SleepDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun onSetSleepQuality(quality: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val tonight = dao.get(sleepNightKey) ?: return@withContext
                tonight.sleepQuality = quality
                dao.update(tonight)
            }

            _navigateToSleepTracker.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
