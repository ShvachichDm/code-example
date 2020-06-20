package io.polydev.public_transport.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.polydev.public_transport.base.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


abstract class BaseViewModel: ViewModel(),CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + Dispatchers.IO

    val toastMessage = MutableLiveData<Event<String>>()


    abstract fun onStart()

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
