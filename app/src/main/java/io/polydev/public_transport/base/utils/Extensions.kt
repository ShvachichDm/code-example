package io.polydev.public_transport.base.utils

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun Context?.toast( textId: Int, duration: Int = Toast.LENGTH_SHORT) = this?.let { Toast.makeText(it, textId, duration).show() }

fun Context?.toast(message: String, duration: Int = Toast.LENGTH_SHORT) = this?.let { Toast.makeText(it, message, duration).show() }

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> LifecycleOwner.observeEvent(liveData: LiveData<Event<T>>, action: (t: Event<T>) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}