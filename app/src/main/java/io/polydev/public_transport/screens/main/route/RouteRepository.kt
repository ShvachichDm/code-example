package io.polydev.public_transport.screens.main.route

import android.util.Log
import io.polydev.public_transport.data_models.BusData
import io.polydev.public_transport.data_models.ReasonData
import io.polydev.public_transport.data_models.RouteData
import io.polydev.public_transport.data_models.TripTimeData
import io.polydev.public_transport.screens.main.route.RouteContract
import io.polydev.public_transport.utils.AppConstants
import io.polydev.public_transport.utils.TokenFormatter
import io.polydev.public_transport.utils.api.ApiClient
import io.polydev.public_transport.utils.data.LocalDataManager
import io.polydev.public_transport.utils.data.PreferencesManager
import io.polydev.public_transport.utils.data.RealmManager
import io.polydev.public_transport.utils.sockets.SocketManager

class RouteRepository: RouteContract.Repository() {

    override suspend fun loadRingTimes(token: String) {
        try {
            val response = ApiClient.getApiService().getCircles(TokenFormatter.formatToken(token))
            if (response.isSuccessful || response.body()!!.success) {
                val data = response.body()!!.data
                if(data != null) {
                    val startTime = data.startTime
                    val ringTimes = ArrayList<TripTimeData>()
                    val circles = data.circles
                    if(!circles.isNullOrEmpty()) {
                        for (index in circles.indices) {
                            ringTimes.add(
                                TripTimeData(
                                    index,
                                    circles[index].startTime,
                                    circles[index].finishTime
                                )
                            )
                        }
                    }
                    viewModel!!.onTripTimesLoaded(ringTimes, startTime)
                }
            } else {
                viewModel!!.onLoadError()
            }
        } catch (e: Exception) {
            Log.d(AppConstants.LOG_TAG_ERROR, "loadRingTimes $e")
            viewModel!!.onLoadError()
        }
    }

    override fun updateRingTimesInDatabase(tripTimes: ArrayList<TripTimeData>) {
        RealmManager.updateRingTimes(tripTimes)
    }

    override suspend fun finish(token: String, reason: String) {
        try {
            val response = ApiClient.getApiService().finish(
                TokenFormatter.formatToken(token),
                FinishRequestBody(reason)
            )
            if (response.isSuccessful) {
                if (response.body()!!.success) {
                    viewModel!!.onFinishSuccess()
                } else {
                    viewModel!!.onFinishFailure()
                }

            } else {
                viewModel!!.onFinishFailure()
            }
        } catch (e: Exception) {
            Log.d(AppConstants.LOG_TAG_ERROR, "finish $e")
            viewModel!!.onFinishFailure()
        }
    }

    override fun getToken(): String {
        return PreferencesManager.getToken()
    }


    override fun subscribeOnRouteEvents() {
        SocketManager.setRouteSocketListener(object : SocketManager.RouteSocketListener {
            override fun onBusData(busData: BusDataEvent) {
                val previousJson = busData.bus.prev
                val currentJson = busData.bus.current
                val nextJson = busData.bus.next
                val previous = if(previousJson != null) {
                    BusData(
                        previousJson.plate,
                        previousJson.time,
                        previousJson.busStop,
                        previousJson.atStop
                    )
                } else {
                    null
                }
                val current = if(currentJson != null) {
                    BusData(
                        currentJson.plate,
                        currentJson.time,
                        currentJson.busStop,
                        currentJson.atStop
                    )
                } else {
                    null
                }
                val next =  if(nextJson != null) {
                    BusData(
                        nextJson.plate,
                        nextJson.time,
                        nextJson.busStop,
                        nextJson.atStop
                    )
                } else {
                    null
                }
                viewModel!!.onBusDataUpdated(previous,current,next)
            }

            override fun onStartCircle(circleEvent: CircleEvent) {
                val ringTimes = ArrayList<TripTimeData>()
                val circles = circleEvent.circles
                if(!circles.isNullOrEmpty()) {
                    for (index in circles.indices) {
                        val ringTimeJsonObject = circles[index]
                        ringTimes.add(
                            TripTimeData(
                                index,
                                ringTimeJsonObject.startTime,
                                ringTimeJsonObject.finishTime
                            )
                        )
                    }
                }
                viewModel!!.onTripStart(ringTimes,circleEvent.startTime)

            }

            override fun onFinishCircle(circleEvent: CircleEvent) {
                val ringTimes = ArrayList<TripTimeData>()
                val circles = circleEvent.circles
                if(!circles.isNullOrEmpty()) {
                    for (index in circles.indices) {
                        val ringTimeJsonObject = circles[index]
                        ringTimes.add(
                            TripTimeData(
                                index,
                                ringTimeJsonObject.startTime,
                                ringTimeJsonObject.finishTime
                            )
                        )
                    }
                }
                viewModel!!.onTripEnd(ringTimes,circleEvent.startTime)
            }

        })
    }

    override fun describeOnRouteEvents() {
        SocketManager.setRouteSocketListener(null)
    }

    override fun getRouteDataFromDatabase(): RouteData? {
        return RealmManager.getData()
    }

    override fun updateSelectedReasonInLocal(reason: ReasonData) {
        LocalDataManager.selectedReason = reason
    }

    override fun getRingTimesFromDatabase(): ArrayList<TripTimeData>? {
        return RealmManager.getRingTimes()
    }

    override fun clearDatabase() {
        RealmManager.clearData()
    }

    override fun deauthorizeAccount() {
        PreferencesManager.updateAuthorizationStatus(false)
    }

}