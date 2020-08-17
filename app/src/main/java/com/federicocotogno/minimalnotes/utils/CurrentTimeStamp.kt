package com.federicocotogno.minimalnotes.utils

import java.text.SimpleDateFormat
import java.util.*

class CurrentTimeStamp {

    fun getCurrentTimeStamp() : String {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val dateTime = Date(currentTime)

        val dateString = dateFormat.format(dateTime)
        val hourString = timeFormat.format(dateTime)

        return "Last edited: $dateString at $hourString"
    }
}