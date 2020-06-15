package io.polydev.public_transport.utils.data

import io.polydev.public_transport.data_models.*

object LocalDataManager {
    var selectedReason: ReasonData? = null
    var mapEvents: ArrayList<MapEventData>? = null
    var locationListener: LocationListener? = null
    var driverLocation: LatLng? = null



    interface LocationListener {
        fun onLocationReceived(lat: Double,lon: Double, accuracy: Float)
    }
}