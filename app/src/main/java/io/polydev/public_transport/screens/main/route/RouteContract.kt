package io.polydev.public_transport.screens.main.route

import io.polydev.public_transport.base.BaseFragment
import io.polydev.public_transport.base.BaseRepository
import io.polydev.public_transport.base.BaseViewModel
import io.polydev.public_transport.base.utils.Event
import io.polydev.public_transport.data_models.BusData
import io.polydev.public_transport.data_models.ReasonData
import io.polydev.public_transport.data_models.RouteData
import io.polydev.public_transport.data_models.TripTimeData
import io.polydev.public_transport.utils.enums.Reasons
import io.polydev.public_transport.utils.enums.RouteTimeColor

interface RouteContract {
    abstract class View(layoutId: Int): BaseFragment<RouteViewModel>(layoutId, RouteViewModel::class.java) {
        abstract fun observeRouteNumber(number: String)
        abstract fun observeTripTimes(tripTimes: ArrayList<TripTimeData>)
        abstract fun observeShowTripTimesPlaceHolder(show: Boolean)
        abstract fun observeShowSelectReasonDialog(show: Boolean)
        abstract fun observePreviousAddress(address: String)
        abstract fun observePreviousTime(time: String)
        abstract fun observePreviousCode(code: String)
        abstract fun observePreviousAtStop(atStop: Boolean?)
        abstract fun observeCurrentAddress(address: String)
        abstract fun observeCurrentTime(time: String)
        abstract fun observeCurrentCode(code: String)
        abstract fun observeCurrentAtStop(atStop: Boolean?)
        abstract fun observeCurrentTimeColor(color: RouteTimeColor)
        abstract fun observeNextAddress(address: String)
        abstract fun observeNextTime(time: String)
        abstract fun observeNextCode(code: String)
        abstract fun observeNextAtStop(atStop: Boolean?)
        abstract fun observeNavigateFinishScreenWithoutReturn(event: Event<Any>)
        abstract fun observeCurrentTripNumber(number: String)
        abstract fun observeCurrentTripTime(startTime: Long)
        abstract fun observeShowTripTimes(show: Boolean)
        abstract fun observeShowCurrentTripTime(show: Boolean)
    }

    abstract class ViewModel: BaseViewModel() {
        abstract val repository: Repository
        override fun onStart() {
            repository.attachViewModel(this)
        }
        override fun onCleared() {
            super.onCleared()
            repository.detachViewModel()
        }
        abstract fun onBtnFinishRouteClick()
        abstract fun onReasonSelected(reason: Reasons)
        abstract fun onTripTimesLoaded(tripTimes: ArrayList<TripTimeData>, startTime: Long?)
        abstract fun onTripStart(tripTimes: ArrayList<TripTimeData>, startTime: Long?)
        abstract fun onTripEnd(tripTimes: ArrayList<TripTimeData>, startTime: Long?)
        abstract fun onRingTimesLoadError()
        abstract fun onBusDataUpdated(previous: BusData?, current: BusData?, next: BusData?)
        abstract fun onFinishSuccess()
        abstract fun onFinishFailure()
        abstract fun onLoadError()
    }

    abstract class Repository: BaseRepository<ViewModel>() {
        abstract suspend fun loadRingTimes(token: String)
        abstract suspend fun finish(token: String, reason: String)
        abstract fun updateRingTimesInDatabase(tripTimes: ArrayList<TripTimeData>)
        abstract fun getToken(): String
        abstract fun subscribeOnRouteEvents()
        abstract fun describeOnRouteEvents()
        abstract fun getRouteDataFromDatabase(): RouteData?
        abstract fun updateSelectedReasonInLocal(reason: ReasonData)
        abstract fun getRingTimesFromDatabase(): ArrayList<TripTimeData>?
        abstract fun clearDatabase()
        abstract fun deauthorizeAccount()
    }
}
