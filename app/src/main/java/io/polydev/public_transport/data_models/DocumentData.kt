package io.polydev.public_transport.data_models

import io.polydev.public_transport.screens.main.profile.models.DocumentModel
import io.realm.RealmObject

open class DocumentData(
    override var imageURL: String
) : RealmObject(),DocumentModel {

    constructor(): this("")
}