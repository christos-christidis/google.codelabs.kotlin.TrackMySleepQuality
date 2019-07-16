package com.kotlin.trackmysleepquality.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.kotlin.trackmysleepquality.R
import com.kotlin.trackmysleepquality.database.SleepDatabase
import com.kotlin.trackmysleepquality.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentSleepQualityBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_quality, container, false)

        val application = activity!!.application
        val dao = SleepDatabase.getInstance(application).sleepDatabaseDao

        val args = SleepQualityFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = SleepQualityViewModelFactory(args.sleepNightKey, dao)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SleepQualityViewModel::class.java)

        binding.viewModel = viewModel
        viewModel.navigateToSleepTracker.observe(this, Observer {
            if (it == true) {
                findNavController().navigate(SleepQualityFragmentDirections
                        .actionSleepQualityToSleepTracker())
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
