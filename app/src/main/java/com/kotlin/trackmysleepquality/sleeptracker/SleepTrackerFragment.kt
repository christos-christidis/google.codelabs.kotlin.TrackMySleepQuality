package com.kotlin.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kotlin.trackmysleepquality.R
import com.kotlin.trackmysleepquality.database.SleepDatabase
import com.kotlin.trackmysleepquality.databinding.FragmentSleepTrackerBinding

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = this.activity!!.application
        val dao = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dao, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SleepTrackerViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setUpRecyclerView(binding.sleepList)

        registerObservers(viewModel, binding.sleepList.adapter as SleepNightAdapter)

        return binding.root
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        val adapter = SleepNightAdapter { nightId ->
            Toast.makeText(context, "$nightId", Toast.LENGTH_LONG).show()
            findNavController().navigate(SleepTrackerFragmentDirections
                    .actionSleepTrackerToSleepDetail(nightId))
        }

        recyclerView.adapter = adapter

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }

        recyclerView.layoutManager = manager
    }

    private fun registerObservers(viewModel: SleepTrackerViewModel, adapter: SleepNightAdapter) {
        viewModel.navigateToSleepQuality.observe(this, Observer { night ->
            night?.let {
                findNavController().navigate(SleepTrackerFragmentDirections
                        .actionSleepTrackerToSleepQuality(night.nightId))
                viewModel.doneNavigatingToSleepQuality()
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

        viewModel.nights.observe(this, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
    }
}
