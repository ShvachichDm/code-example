package io.polydev.public_transport.data_models

import io.realm.RealmList
import io.realm.RealmObject

open class ProfileData(
    var name: String,
    var birthdayTimestamp: Long,
    var driverLicense: String,
    var driverLicenseValidity: Long,
    var passportValidity: Long,
    var history: RealmList<RouteHistoryData>
): RealmObject() {

    constructor(): this(
        "",
        0,
        "",
        0,
        0,
        RealmList()
    )
}