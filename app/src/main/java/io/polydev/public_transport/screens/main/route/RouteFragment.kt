package io.polydev.public_transport.screens.main.route

import android.content.Intent
import android.os.SystemClock
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.polydev.public_transport.R
import io.polydev.public_transport.base.utils.Event
import io.polydev.public_transport.base.utils.observe
import io.polydev.public_transport.data_models.TripTimeData
import io.polydev.public_transport.di.ComponentManager
import io.polydev.public_transport.screens.main.route.adapters.TripTimesRecyclerViewAdapter
import io.polydev.public_transport.screens.main.route.models.TripTimeModel
import io.polydev.public_transport.utils.enums.Reasons
import io.polydev.public_transport.utils.enums.RouteTimeColor
import kotlinx.android.synthetic.main.dialog_select_reason.view.*
import kotlinx.android.synthetic.main.fragment_route.view.*
import kotlinx.android.synthetic.main.view_current_trip_time.view.*
import kotlinx.coroutines.launch

class RouteFragment : RouteContract.View(R.layout.fragment_route) {

    private lateinit var tripTimesAdapter: TripTimesRecyclerViewAdapter
    private lateinit var selectReasonDialog: BottomSheetDialog

    override fun injectComponent() {
        ComponentManager.getRouteComponent().inject(this)
    }

    override fun clearComponent() {
        ComponentManager.clearRouteComponent()
    }

    override fun setUpUI(view: View) {
        setUpDialog()
        setUpAdapters()
        setUpRecyclerViews(view)
        setUpListeners(view)
    }

    private fun setUpDialog() {
        selectReasonDialog = BottomSheetDialog(context!!)
        val view = layoutInflater.inflate(R.layout.dialog_select_reason, null)
        view.apply {
            llOnSchedule.setOnClickListener {
                viewModel.onReasonSelected(Reasons.SHEDULED)
            }
            llAccident.setOnClickListener {
                viewModel.onReasonSelected(Reasons.ACCIDENT)
            }
            llForHealth.setOnClickListener {
                viewModel.onReasonSelected(Reasons.FOR_HEALTH)
            }
            llCrash.setOnClickListener {
                viewModel.onReasonSelected(Reasons.CRASH)
            }
            llAnother.setOnClickListener {
                viewModel.onReasonSelected(Reasons.ANOTHER)
            }
        }
        selectReasonDialog.setContentView(view)
    }

    private fun setUpAdapters() {
        tripTimesAdapter = TripTimesRecyclerViewAdapter()
    }

    private fun setUpRecyclerViews(view: View) {
        view.rvRingTimes.apply {
            adapter = tripTimesAdapter
        }
    }

    private fun setUpListeners(view: View) {
        view.apply {
            fabFinishRoute.setOnClickListener {
                viewModel.onBtnFinishRouteClick()
            }
        }

    }

    override fun observeViewModel() {
        observe(viewModel.routeNumber, this::observeRouteNumber)
        observe(viewModel.showSelectReasonDialog, this::observeShowSelectReasonDialog)
        observe(viewModel.showCurrentTripTime, this::observeShowCurrentTripTime)
        observe(viewModel.currentTripNumber, this::observeCurrentTripNumber)
        observe(viewModel.currentTripStartTime, this::observeCurrentTripTime)
        observe(viewModel.currentCode, this::observeCurrentCode)
        observe(viewModel.previousCode, this::observePreviousCode)
        observe(viewModel.nextCode, this::observeNextCode)
        observe(viewModel.currentAddress, this::observeCurrentAddress)
        observe(viewModel.previousAddress, this::observePreviousAddress)
        observe(viewModel.nextAddress, this::observeNextAddress)
        observe(viewModel.currentTime, this::observeCurrentTime)
        observe(viewModel.currentTimeColor, this::observeCurrentTimeColor)
        observe(viewModel.previousTime, this::observePreviousTime)
        observe(viewModel.nextTime, this::observeNextTime)
        observe(viewModel.currentAtStop, this::observeCurrentAtStop)
        observe(viewModel.previousAtStop, this::observePreviousAtStop)
        observe(viewModel.nextAtStop, this::observeNextAtStop)
        observe(
            viewModel.navigateFinishScreenWithoutReturn,
            this::observeNavigateFinishScreenWithoutReturn
        )
        observe(viewModel.showTripTimes, this::observeShowTripTimes)
        observe(viewModel.tripTimes, this::observeTripTimes)
        observe(viewModel.showTripTimesPlaceHolder, this::observeShowTripTimesPlaceHolder)
    }

    override fun observeShowCurrentTripTime(show: Boolean) {
        launch {
            if (show) {
                view?.vCurrentTripTime?.apply {
                    visibility = View.VISIBLE
                }
            } else {
                view?.vCurrentTripTime?.apply {
                    visibility = View.GONE
                }
            }
        }
    }

    override fun observeCurrentTripNumber(number: String) {
        launch {
            view?.vCurrentTripTime?.apply {
                tvNumber.text = number
            }
        }
    }

    override fun observeRouteNumber(number: String) {
        launch {
            view?.apply {
                toolbar.title = getString(R.string.route_number, number)
            }
        }
    }

    override fun observeShowSelectReasonDialog(show: Boolean) {
        launch {
            if (show) {
                selectReasonDialog.show()
            } else {
                selectReasonDialog.dismiss()
            }
        }
    }

    override fun observeCurrentTripTime(startTime: Long) {
        launch {
            view?.vCurrentTripTime?.apply {
                chronometer.apply {
                    base = SystemClock.elapsedRealtime() + startTime - System.currentTimeMillis()
                    start()
                }
            }
        }
    }

    override fun observeCurrentCode(code: String) {
        launch {
            view!!.tvCodeCurrent.text = code
        }
    }

    override fun observePreviousCode(code: String) {
        launch {
            view!!.tvCodePrevious.text = code
        }
    }

    override fun observeNextCode(code: String) {
        launch {
            view!!.tvCodeNext.text = code
        }
    }

    override fun observeCurrentAddress(address: String) {
        launch {
            view!!.tvAddressCurrent.text = address
        }
    }

    override fun observePreviousAddress(address: String) {
        launch {
            view!!.tvAddressPrevious.text = address
        }
    }

    override fun observeNextAddress(address: String) {
        launch {
            view!!.tvAddressNext.text = address
        }
    }

    override fun observeCurrentTime(time: String) {
        launch {
            view!!.tvTimeCurrent.text = time
        }
    }

    override fun observeCurrentTimeColor(color: RouteTimeColor) {
        launch {
            when (color) {
                RouteTimeColor.RED -> {
                    view!!.tvTimeCurrent.setTextColor(
                        ContextCompat.getColorStateList(
                            context!!,
                            R.color.colorRed
                        )
                    )
                }
                RouteTimeColor.GREEN -> {
                    view!!.tvTimeCurrent.setTextColor(
                        ContextCompat.getColorStateList(
                            context!!,
                            R.color.colorAccent
                        )
                    )
                }
                else -> {
                    view!!.tvTimeCurrent.setTextColor(
                        ContextCompat.getColorStateList(
                            context!!,
                            R.color.colorOnSurfaceSecond
                        )
                    )
                }
            }
        }
    }


    override fun observePreviousTime(time: String) {
        launch {
            view!!.tvTimePrevious.text = time
        }
    }

    override fun observeNextTime(time: String) {
        launch {
            view!!.tvTimeNext.text = time
        }
    }

    override fun observeCurrentAtStop(atStop: Boolean?) {
        launch {
            when (atStop) {
                null -> {
                    view!!.ivAtStopCurrent.visibility = View.GONE

                }
                true -> {
                    view!!.ivAtStopCurrent.apply {
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.ic_at_stop)
                    }
                }
                false -> {
                    view!!.ivAtStopCurrent.apply {
                        visibility = View.VISIBLE
                        ivAtStopCurrent.setImageResource(R.drawable.ic_not_at_stop)
                    }
                }
            }
        }
    }

    override fun observePreviousAtStop(atStop: Boolean?) {
        launch {
            when (atStop) {
                null -> {
                    view!!.ivAtStopPrevious.visibility = View.GONE
                }
                true -> {
                    view!!.ivAtStopPrevious.apply {
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.ic_at_stop)
                    }
                }
                false -> {
                    view!!.ivAtStopPrevious.apply {
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.ic_not_at_stop)
                    }
                }
            }
        }
    }

    override fun observeNextAtStop(atStop: Boolean?) {
        launch {
            when (atStop) {
                null -> {
                    view!!.ivAtStopNext.visibility = View.GONE
                }
                true -> {
                    view!!.ivAtStopNext.apply {
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.ic_at_stop)
                    }
                }
                false -> {
                    view!!.ivAtStopNext.apply {
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.ic_not_at_stop)
                    }
                }
            }
        }
    }

    override fun observeNavigateFinishScreenWithoutReturn(event: Event<Any>) {
        launch {
            startActivity(Intent(context, FinishActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }

    override fun observeShowTripTimes(show: Boolean) {
        launch {
            if (show) {
                view?.rvRingTimes?.visibility = View.VISIBLE
            } else {
                view?.rvRingTimes?.visibility = View.GONE
            }
        }
    }

    override fun observeTripTimes(tripTimes: ArrayList<TripTimeData>) {
        launch {
            tripTimesAdapter.updateModels(tripTimes as ArrayList<TripTimeModel>)
        }
    }

    override fun observeShowTripTimesPlaceHolder(show: Boolean) {
        launch {
            if (show) {
                view?.tvPlaceholder?.visibility = View.VISIBLE
            } else {
                view?.tvPlaceholder?.visibility = View.GONE
            }

        }
    }


}



