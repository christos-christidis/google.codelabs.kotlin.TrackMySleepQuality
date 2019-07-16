package com.kotlin.trackmysleepquality.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kotlin.trackmysleepquality.R
import com.kotlin.trackmysleepquality.convertDurationToFormatted
import com.kotlin.trackmysleepquality.convertNumericQualityToString
import com.kotlin.trackmysleepquality.database.SleepNight

// SOS: search layout eg for app:sleepDurationFormatted.
// There's no advantage to doing it this way AFAIK, it seems to me that binding the traditional way
// is clearer. But I must know this technique too in case I encounter it
@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(night: SleepNight) {
    text = convertDurationToFormatted(night.startTimeMilli, night.endTimeMilli, context.resources)
}

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(night: SleepNight) {
    text = convertNumericQualityToString(night.sleepQuality, context.resources)
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(night: SleepNight) {
    setImageResource(when (night.sleepQuality) {
        0 -> R.drawable.ic_sleep_0
        1 -> R.drawable.ic_sleep_1
        2 -> R.drawable.ic_sleep_2
        3 -> R.drawable.ic_sleep_3
        4 -> R.drawable.ic_sleep_4
        5 -> R.drawable.ic_sleep_5
        else -> R.drawable.ic_sleep_active
    })
}
