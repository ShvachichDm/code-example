package io.polydev.public_transport.data_models

import io.polydev.public_transport.screens.main.route.models.TripTimeModel
import io.realm.RealmObject

open class TripTimeData(
    var index: Int,
    var startTime: Long,
    var endTime: Long
): RealmObject(), TripTimeModel {
    override val number: String
        get() = "#${index + 1}"
    override val time: String
        get() {
            val amountMilliseconds = endTime - startTime
            val amountSeconds = amountMilliseconds / 1000
            val amountMinutes = amountSeconds / 60
            val amountHours = amountMinutes / 60
            val seconds = amountSeconds - (amountMinutes * 60)
            val minutes = amountMinutes - (amountHours * 60)
            val hours = amountHours - amountMinutes / 60
            val secondsStr = if(seconds < 10) "0$seconds" else seconds.toString()
            val minutesStr = if(minutes < 10) "0$minutes" else minutes.toString()
            val hoursStr = if(hours < 10) "0$hours" else hours.toString()
            return "$hoursStr:$minutesStr:$secondsStr"
        }

    constructor(): this(0,0,0)
}