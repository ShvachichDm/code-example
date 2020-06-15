package io.polydev.public_transport.data_models

import io.realm.RealmObject


open class ReasonData(
     var reason: String
): RealmObject() {

    constructor(): this(
        ""
    )
}