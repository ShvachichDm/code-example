package io.polydev.public_transport.data_models

import io.realm.RealmObject

open class TagData(
    var busStop: Boolean,
    var lat: String,
    var lon: String
): RealmObject() {

    constructor(): this(
        false,
        "",""
    )
}