package com.kotlin.trackmysleepquality.sleepdetail

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
import com.kotlin.trackmysleepquality.databinding.FragmentSleepDetailBinding

class SleepDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentSleepDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_detail, container, false)

        val application = activity!!.application
        val args = SleepDetailFragmentArgs.fromBundle(arguments!!)

        val dao = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = SleepDetailViewModelFactory(args.sleepNightKey, dao)

        val sleepDetailViewModel = ViewModelProviders.of(
                this, viewModelFactory).get(SleepDetailViewModel::class.java)

        binding.viewModel = sleepDetailViewModel
        binding.lifecycleOwner = this

        sleepDetailViewModel.navigateToSleepTracker.observe(this, Observer {
            if (it == true) {
                findNavController().navigate(
                        SleepDetailFragmentDirections.actionSleepDetailToSleepTracker())
                sleepDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }
}