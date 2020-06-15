package io.polydev.public_transport.data_models

import android.nfc.Tag
import io.realm.RealmList
import io.realm.RealmObject

open class RouteData (
    var id: String,
    var number: String,
    var forwardGeoTags: RealmList<TagData>,
    var backwardGeoTags: RealmList<TagData>

): RealmObject() {

    constructor(): this(
        "",
        "",
         RealmList(),
         RealmList()
    )
}