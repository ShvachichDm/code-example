package io.polydev.public_transport.data_models

data class BusData(
    val plate: String,
    val time: Long?,
    val busStop: String?,
    val atStop: Boolean?
)
