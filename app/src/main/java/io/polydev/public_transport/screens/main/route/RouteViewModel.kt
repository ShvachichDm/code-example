package io.polydev.public_transport.screens.main.route

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.polydev.public_transport.base.utils.Event
import io.polydev.public_transport.data_models.BusData
import io.polydev.public_transport.data_models.ReasonData
import io.polydev.public_transport.data_models.TripTimeData
import io.polydev.public_transport.utils.AppConstants
import io.polydev.public_transport.utils.enums.Reasons
import io.polydev.public_transport.utils.enums.RouteTimeColor
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

class RouteViewModel @Inject constructor(): RouteContract.ViewModel() {
    
    override val repository: RouteContract.Repository = RouteRepository()

    override var routeNumber = MutableLiveData<String>()
    override var tripTimes  = MutableLiveData<ArrayList<TripTimeData>>()
    override var showTripTimesPlaceHolder  = MutableLiveData<Boolean>()
    override var showSelectReasonDialog = MutableLiveData<Boolean>()
    override var previousAddress = MutableLiveData<String>()
    override var previousTime = MutableLiveData<String>()
    override var previousCode  = MutableLiveData<String>()
    override var previousAtStop = MutableLiveData<Boolean?>()
    override var currentAddress = MutableLiveData<String>()
    override var currentTime = MutableLiveData<String>()
    override var currentCode  = MutableLiveData<String>()
    override var currentAtStop = MutableLiveData<Boolean?>()
    override var currentTimeColor = MutableLiveData<RouteTimeColor>()
    override var nextAddress = MutableLiveData<String>()
    override var nextTime = MutableLiveData<String>()
    override var nextCode  = MutableLiveData<String>()
    override var nextAtStop = MutableLiveData<Boolean?>()
    override var navigateFinishScreenWithoutReturn = MutableLiveData<Event<Any>>()
    override var currentTripNumber = MutableLiveData<String>()
    override var currentTripStartTime = MutableLiveData<Long>()
    override var showTripTimes = MutableLiveData<Boolean>()
    override var showCurrentTripTime = MutableLiveData<Boolean>()

    override fun onStart() {
        super.onStart()
        Log.d(AppConstants.LOG_TAG_1,"RouteViewModel onStart")
        repository.subscribeOnRouteEvents()
        val route = repository.getRouteDataFromDatabase()
        if(route != null) {
            routeNumber.value = route.number
        }
        val ringTimes = repository.getRingTimesFromDatabase()
        if(!ringTimes.isNullOrEmpty()) {
            showTripTimesPlaceHolder.value = false
            showTripTimes.value = true
            tripTimes.value = ringTimes
        } else {
            showTripTimesPlaceHolder.value = true
        }

        launch {
            repository.loadRingTimes(repository.getToken())
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.describeOnRouteEvents()
    }

    override fun onBtnFinishRouteClick() {
        showSelectReasonDialog.value = true
    }

    override fun onReasonSelected(reason: Reasons) {
        showSelectReasonDialog.value = false
        repository.updateSelectedReasonInLocal(ReasonData(reason.name))
        launch {
            repository.finish(repository.getToken(),reason.name)
        }
    }

    override fun onTripTimesLoaded(tripTimes: ArrayList<TripTimeData>, startTime: Long?) {
        repository.updateRingTimesInDatabase(tripTimes)
        if(tripTimes.isNotEmpty()) {
            showTripTimesPlaceHolder.value = false
            showTripTimes.value = true
            this.tripTimes.value = tripTimes
            currentTripStartTime.value = tripTimes.last().endTime
            currentTripNumber.value = "#${tripTimes.size + 1}"
        } else {
            showTripTimes.value = false
            if(startTime != null) {
                currentTripStartTime.value = tripTimes.last().endTime
                currentTripNumber.value = "#${tripTimes.size + 1}"
                showTripTimesPlaceHolder.value = false
            } else {
                showTripTimesPlaceHolder.value = true
                showCurrentTripTime.value = true
            }
        }
    }

    override fun onTripStart(tripTimes: ArrayList<TripTimeData>, startTime: Long?) {
        repository.updateRingTimesInDatabase(tripTimes)
        if(tripTimes.isNotEmpty()) {
            showTripTimesPlaceHolder.value = false
            showTripTimes.value = true
            this.tripTimes.value = tripTimes
            currentTripStartTime.value = tripTimes.last().endTime
            currentTripNumber.value = "#${tripTimes.size + 1}"
        } else {
            showTripTimes.value = false
            if(startTime != null) {
                currentTripStartTime.value = tripTimes.last().endTime
                currentTripNumber.value = "#${tripTimes.size + 1}"
                showTripTimesPlaceHolder.value = false
            } else {
                showTripTimesPlaceHolder.value = true
                showCurrentTripTime.value = false
            }
        }
    }

    override fun onTripEnd(tripTimes: ArrayList<TripTimeData>, startTime: Long?) {
        repository.updateRingTimesInDatabase(tripTimes)
        if(tripTimes.isNotEmpty()) {
            showTripTimesPlaceHolder.value = false
            showTripTimes.value = true
            this.tripTimes.value = tripTimes
        } else {
            showTripTimes.value = false
            showTripTimesPlaceHolder.value = true
        }
        showCurrentTripTime.value = false
    }

    override fun onRingTimesLoadError() {
        toastMessage.value = Event("Не удалось загрузить время кругов")
    }

    override fun onBusDataUpdated(previous: BusData?, current: BusData?, next: BusData?) {
        val shortDottedLine = "- - - - -"
        val longDottedLine = "- - - - - - - - -"
        if(previous != null) {
            if(previous.busStop != null) {
                previousAddress.value = previous.busStop
            } else {
                previousAddress.value = longDottedLine
            }
            if(previous.time != null)  {
                var seconds = abs(previous.time / 1000)
                val minutes = abs( seconds / 60)
                seconds -= minutes * 60
                val secondsStr = if(seconds < 10) "0$seconds" else seconds.toString()
                val minutesStr = if(minutes < 10) "0$minutes" else minutes.toString()
                var time = "$minutesStr:$secondsStr"
                time = if(previous.time < 0){
                    "-$time"
                } else {
                    "+$time"
                }
                previousTime.value = time
            } else {
                previousTime.value = shortDottedLine
            }
            previousCode.value = previous.plate
            previousAtStop.value = previous.atStop
        } else {
            previousAtStop.value = null
            previousAddress.value = longDottedLine
            previousTime.value = shortDottedLine
            previousCode.value = shortDottedLine
        }
        if(current != null) {
            if(current.busStop != null) {
                currentAddress.value = current.busStop
            } else {
                currentAddress.value = longDottedLine
            }
            if(current.time != null)  {
                var seconds = abs(current.time / 1000)
                val minutes = abs( seconds / 60)
                seconds -= minutes * 60
                val secondsStr = if(seconds < 10) "0$seconds" else seconds.toString()
                val minutesStr = if(minutes < 10) "0$minutes" else minutes.toString()
                var time = "$minutesStr:$secondsStr"
                var color = RouteTimeColor.GREEN
                time = if(current.time < 0){
                    color = RouteTimeColor.RED
                    "-$time"
                } else {
                    "+$time"
                }
                currentTime.value = time
                currentTimeColor.value = color
            } else {
                currentTime.value = shortDottedLine
                currentTimeColor.value = RouteTimeColor.DEFAULT
            }
            currentCode.value = current.plate
            currentAtStop.value = current.atStop

        } else {
            currentAtStop.value = null
            currentAddress.value = longDottedLine
            currentTime.value = shortDottedLine
            currentCode.value = shortDottedLine
            currentTimeColor.value = RouteTimeColor.DEFAULT

        }
        if(next != null) {

            if(next.busStop != null) {
                nextAddress.value = next.busStop
            } else {
                nextAddress.value = longDottedLine
            }
            if(next.time != null)  {
                var seconds = abs(next.time / 1000)
                val minutes = abs( seconds / 60)
                seconds -= minutes * 60
                val secondsStr = if(seconds < 10) "0$seconds" else seconds.toString()
                val minutesStr = if(minutes < 10) "0$minutes" else minutes.toString()
                var time = "$minutesStr:$secondsStr"
                time = if(next.time < 0){
                    "-$time"
                } else {
                    "+$time"
                }
                nextTime.value = time
            } else {
                nextTime.value = shortDottedLine
            }
            nextCode.value = next.plate
            nextAtStop.value = next.atStop
        } else {
            nextAtStop.value = null
            nextAddress.value = longDottedLine
            nextTime.value = shortDottedLine
            nextCode.value = shortDottedLine
        }
    }
    override fun onFinishSuccess() {
        repository.clearDatabase()
        repository.deauthorizeAccount()
        navigateFinishScreenWithoutReturn.postValue(Event(Unit))
    }

    override fun onFinishFailure() {
        toastMessage.value = Event("Не удалось завершить маршрут, повторите попытку позже")
    }

    override fun onLoadError() {

    }
}

