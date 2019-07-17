package com.kotlin.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.trackmysleepquality.database.SleepDatabaseDao

// This is pretty much boiler plate code for a ViewModel Factory.
class SleepTrackerViewModelFactory(
        private val dao: SleepDatabaseDao,
        private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepTrackerViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return SleepTrackerViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

