package io.polydev.public_transport.data_models

import io.realm.RealmObject

open class LatLng(
    var lat: String,
    var lon: String
): RealmObject() {

    constructor(): this("","")
}