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
    abstract class View(layoutId: Int): BaseFragment<ViewModel>(layoutId, ViewModel::class.java) {
        protected abstract fun observeRouteNumber(number: String)
        protected abstract fun observeTripTimes(tripTimes: ArrayList<TripTimeData>)
        protected abstract fun observeShowTripTimesPlaceHolder(show: Boolean)
        protected abstract fun observeShowSelectReasonDialog(show: Boolean)
        protected abstract fun observePreviousAddress(address: String)
        protected abstract fun observePreviousTime(time: String)
        protected abstract fun observePreviousCode(code: String)
        protected abstract fun observePreviousAtStop(atStop: Boolean?)
        protected abstract fun observeCurrentAddress(address: String)
        protected abstract fun observeCurrentTime(time: String)
        protected abstract fun observeCurrentCode(code: String)
        protected abstract fun observeCurrentAtStop(atStop: Boolean?)
        protected abstract fun observeCurrentTimeColor(color: RouteTimeColor)
        protected abstract fun observeNextAddress(address: String)
        protected abstract fun observeNextTime(time: String)
        protected abstract fun observeNextCode(code: String)
        protected abstract fun observeNextAtStop(atStop: Boolean?)
        protected abstract fun observeNavigateFinishScreenWithoutReturn(event: Event<Any>)
        protected abstract fun observeCurrentTripNumber(number: String)
        protected abstract fun observeCurrentTripTime(startTime: Long)
        protected abstract fun observeShowTripTimes(show: Boolean)
        protected abstract fun observeShowCurrentTripTime(show: Boolean)
    }

    abstract class ViewModel: BaseViewModel() {
        abstract val repository: Repository
        abstract var routeNumber: MutableLiveData<String>
        abstract var tripTimes : MutableLiveData<ArrayList<TripTimeData>>
        abstract var showTripTimesPlaceHolder : MutableLiveData<Boolean>
        abstract var showSelectReasonDialog: MutableLiveData<Boolean>
        abstract var previousAddress: MutableLiveData<String>
        abstract var previousTime: MutableLiveData<String>
        abstract var previousCode : MutableLiveData<String>
        abstract var previousAtStop: MutableLiveData<Boolean?>
        abstract var currentAddress: MutableLiveData<String>
        abstract var currentTime: MutableLiveData<String>
        abstract var currentCode : MutableLiveData<String>
        abstract var currentAtStop: MutableLiveData<Boolean?>
        abstract var currentTimeColor: MutableLiveData<RouteTimeColor>
        abstract var nextAddress: MutableLiveData<String>
        abstract var nextTime: MutableLiveData<String>
        abstract var nextCode : MutableLiveData<String>
        abstract var nextAtStop: MutableLiveData<Boolean?>
        abstract var navigateFinishScreenWithoutReturn: MutableLiveData<Event<Any>>
        abstract var currentTripNumber: MutableLiveData<String>
        abstract var currentTripStartTime: MutableLiveData<Long>
        abstract var showTripTimes: MutableLiveData<Boolean>
        abstract var showCurrentTripTime: MutableLiveData<Boolean>
        
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
