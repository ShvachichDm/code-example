package io.polydev.public_transport.data_models


import io.polydev.public_transport.screens.main.profile.models.RouteHistoryModel
import io.realm.RealmObject
import java.text.SimpleDateFormat
import java.util.*

open class RouteHistoryData(
    var id: String,
     var startTime: Long?,
     var finishTime: Long?
): RealmObject(), RouteHistoryModel {
    override val start: String
        get() {
            return if(startTime == null) {
                "Не начат"
            } else {
                SimpleDateFormat("hh:mm").format(Date(startTime!!))
            }
        }
    override val finish: String
        get() {
            return if(finishTime == null) {
                "Не закончен"
            } else {
                SimpleDateFormat("hh:mm").format(Date(finishTime!!))
            }
        }
    override val date: String
        get() {
            return if(startTime == null) {
                ""
            } else {
                SimpleDateFormat("dd.MM.yyyy").format(Date(startTime!!))
            }
        }
    constructor(): this("",0,0)
}