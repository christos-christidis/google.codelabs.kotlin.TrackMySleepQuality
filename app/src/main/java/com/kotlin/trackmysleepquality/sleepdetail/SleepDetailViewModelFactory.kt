package com.kotlin.trackmysleepquality.sleepdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.trackmysleepquality.database.SleepDatabaseDao

class SleepDetailViewModelFactory(
        private val sleepNightKey: Long,
        private val dao: SleepDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepDetailViewModel::class.java)) {
            return SleepDetailViewModel(sleepNightKey, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}