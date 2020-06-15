package io.polydev.public_transport.services

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.polydev.public_transport.R
import io.polydev.public_transport.utils.Splash
import io.polydev.public_transport.utils.data.LocalDataManager
import io.polydev.public_transport.utils.sockets.SocketManager
import kotlinx.coroutines.*


class SocketService : Service(),CoroutineScope {

    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job


    companion object {
        private const val LOCATION_INTERVAL = 3000L
        private const val NOTIFICATION_ID = 123142323
        private const val CHANNEL_ID = "SocketService"

    }

    private var currentDriverPosition: Location? = null
    private val locationRequest by lazy {
        LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = LOCATION_INTERVAL
        }
    }

    private val locationCallback by lazy {
        object: LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)
                if(result != null) {
                    currentDriverPosition = result.lastLocation
                    if(currentDriverPosition != null) {
                        LocalDataManager.locationListener?.onLocationReceived(
                            currentDriverPosition!!.latitude,
                            currentDriverPosition!!.longitude,
                            currentDriverPosition!!.accuracy
                        )
                    }
                }
            }
        }
    }


    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    private var alreadyStarted = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if(!alreadyStarted) {
            alreadyStarted = true
            startForeground(NOTIFICATION_ID, createNotification())
        }
        return START_STICKY
    }

    override fun onCreate() {
        setUpSocketSupport()
        setUpLocationListener()
        setUpSendingPosition()
    }

    private fun setUpSendingPosition() {
        launch {
            while (true) {
                delay(5000)
                if(SocketManager.socketConnected() && currentDriverPosition != null) {
                    if(currentDriverPosition!!.accuracy < 20) {
                        SocketManager.sendGpsData(
                            currentDriverPosition!!.latitude.toString(),
                            currentDriverPosition!!.longitude.toString()
                        )
                    }
                }
            }
        }
    }
    private fun setUpLocationListener() {
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
            locationRequest, locationCallback , Looper.myLooper()
        )
    }

    private fun setUpSocketSupport() {
        launch {
            while (true) {
                connectSocket()
                delay(10000)
            }
        }
    }
    override fun onDestroy() {
        disconnectSocket()
        job.cancel()
    }


    private fun connectSocket() {
        if (!SocketManager.socketConnected()) {
            SocketManager.connect()
        }
    }

    private fun disconnectSocket() {
        if (SocketManager.socketConnected()) {
            SocketManager.disconnect()
        }
    }

    private fun createNotification(): Notification? {
        createNotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_name),
            getString(R.string.notification_description)
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_description))
            .setContentIntent(openActivityIntent())
            .setStyle(NotificationCompat.BigTextStyle())
            .setSmallIcon(R.drawable.ic_bus)
            .build()
    }

    private fun openActivityIntent(): PendingIntent {
        val intent = Intent(applicationContext, Splash::class.java)
        return PendingIntent.getActivity(applicationContext, 99, intent, 0)
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance)
            channel.description = description
            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


}