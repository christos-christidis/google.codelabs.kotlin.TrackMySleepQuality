package com.kotlin.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.trackmysleepquality.R
import com.kotlin.trackmysleepquality.convertDurationToFormatted
import com.kotlin.trackmysleepquality.convertNumericQualityToString
import com.kotlin.trackmysleepquality.database.SleepNight

class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {

    var nights = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // SOS: inflating is a job for the viewHolder, not the Adapter, so I moved that work in the from
    // function which is a static method inside ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val night = nights[position]
        holder.bind(night)
    }

    // SOS: an extension function. I could have defined it directly inside ViewHolder too
    private fun ViewHolder.bind(night: SleepNight) {
        val res = itemView.context.resources
        sleepLength.text = convertDurationToFormatted(night.startTimeMilli, night.endTimeMilli, res)
        quality.text = convertNumericQualityToString(night.sleepQuality, res)
        qualityImage.setImageResource(when (night.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    }

    override fun getItemCount() = nights.size

    // SOS: since the constructor is called only in from(), I make it private
    class ViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {

        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                return ViewHolder(view)
            }
        }
    }
}
