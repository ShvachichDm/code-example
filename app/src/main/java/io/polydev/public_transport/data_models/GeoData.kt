package io.polydev.public_transport.data_models

data class GeoData(
    val id: String,
    val lat: String,
    val lon: String,
    val direction: Boolean,
    val routeDeviation: Boolean,
    val plate: String
)