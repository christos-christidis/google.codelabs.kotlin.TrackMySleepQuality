package com.kotlin.trackmysleepquality.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.trackmysleepquality.database.SleepDatabaseDao

class SleepQualityViewModelFactory(
        private val sleepNightKey: Long,
        private val dao: SleepDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityViewModel::class.java)) {
            return SleepQualityViewModel(sleepNightKey, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
