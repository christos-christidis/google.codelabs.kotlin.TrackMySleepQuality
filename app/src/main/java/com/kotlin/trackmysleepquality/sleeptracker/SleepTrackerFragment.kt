package com.kotlin.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kotlin.trackmysleepquality.R
import com.kotlin.trackmysleepquality.database.SleepDatabase
import com.kotlin.trackmysleepquality.databinding.FragmentSleepTrackerBinding

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = activity!!.application
        val dao = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dao, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SleepTrackerViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.navigateToSleepQuality.observe(this, Observer { night ->
            night?.let {
                findNavController().navigate(SleepTrackerFragmentDirections
                        .actionSleepTrackerToSleepQuality(night.nightId))
                viewModel.doneNavigating()
            }
        })

        viewModel.showSnackBarEvent.observe(this, Observer {
            if (it) {
                Snackbar.make(view!!,
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT)
                        .show()
                viewModel.doneShowingSnackBar()
            }
        })

        val adapter = SleepNightAdapter()
        binding.sleepList.adapter = adapter
        binding.sleepList.layoutManager = GridLayoutManager(activity, 3)

        viewModel.nights.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}
