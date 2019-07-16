package com.kotlin.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.trackmysleepquality.database.SleepNight
import com.kotlin.trackmysleepquality.databinding.ListItemSleepNightBinding

// SOS: ListAdapter handles all item diffs, removals etc automatically. For that it uses DiffUtil.
// For my part, I simply have to call submitList on the adapter whenever sth changes! Note: this is
// way faster for large lists. Also compare w previous version to see how much code I've deleted
class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val night = getItem(position)
        holder.bind(night)
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                // SOS: Note the binding class I use to inflate
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private fun ViewHolder.bind(night: SleepNight) {
        binding.night = night
        // SOS: good idea to always call this when using binding adapters. Speeds things up
        binding.executePendingBindings()
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }
}
