package io.polydev.public_transport.data_models

import io.realm.RealmObject

open class SituationData(
    var situation: String
): RealmObject() {

    constructor(): this(
        ""
    )
}

