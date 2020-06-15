package io.polydev.public_transport.data_models

import io.polydev.public_transport.screens.main.notifications.models.NotificationModel
import io.realm.RealmObject

open class NotificationData(
    override var message: String,
    override var time: String

): RealmObject(), NotificationModel {

    constructor(): this(
        "",
        ""
    )
}