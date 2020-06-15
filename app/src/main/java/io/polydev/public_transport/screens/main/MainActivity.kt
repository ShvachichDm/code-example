package io.polydev.public_transport.screens.main

//import com.yandex.mapkit.MapKitFactory
//import com.yandex.mapkit.directions.DirectionsFactory


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.polydev.public_transport.R
import io.polydev.public_transport.screens.main.map.MapFragment
import io.polydev.public_transport.screens.main.notifications.NotificationsFragment
import io.polydev.public_transport.screens.main.profile.ProfileFragment
import io.polydev.public_transport.services.SocketService
import io.polydev.public_transport.utils.AppConstants
import io.polydev.public_transport.utils.data.PreferencesManager
import io.polydev.public_transport.utils.sockets.SocketManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job
    private var serviceSupportJob: Job? = null

    private var lastActiveFragmentTag = ""
    private var countUnreadNotifications = 0


    private var networkCallback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d(AppConstants.LOG_TAG_1, "onAvailable")
            }

            override fun onLost(network: Network) {
                Log.d(AppConstants.LOG_TAG_2, "onLost")
                val alertDialog = AlertDialog.Builder(this@MainActivity).create()
                alertDialog.setTitle("Проблемы с соединением")
                alertDialog.setMessage("Нет подключения к интернету")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpUI()
        startSocketService()
    }

    override fun onStop() {
        super.onStop()
        serviceSupportJob?.cancel()
        if(!PreferencesManager.getAuthorizationStatus()) {
            stopSocketService()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    private fun startSocketService() {
        ContextCompat.startForegroundService(this,Intent(this, SocketService::class.java))
    }

    private fun stopSocketService() {
        stopService(Intent(this,SocketService::class.java))
    }
    private fun setUpUI() {
        setUpFragments()
        setUpListeners()
        setUpSocketListener()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    private fun setUpSocketListener() {
        SocketManager.setGeneralSocketListener(object : SocketManager.GeneralSocketListener {


            override fun onConnect() {
                SocketManager.authenticate(PreferencesManager.getToken())
            }

            override fun onDisconnect() {}

            override fun onAuthenticated() {}

            override fun onUnAuthenticated() {}

            override fun onError() {}

            override fun onNotification(notificationEvent: NotificationEvent) {
                launch {
                    countUnreadNotifications += 1
                    if (lastActiveFragmentTag != R.id.iNotifications.toString()) {
                        showNotificationBadge()
                    } else {
                        countUnreadNotifications = 0
                        bnvTabs.removeBadge(R.id.iNotifications)
                    }

                }

            }

        })
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }


    private fun setUpFragments() {


        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer, ProfileFragment(),
                R.id.iProfile.toString()
            )
            .hide(ProfileFragment())
            .commitNow()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer, NotificationsFragment(),
                R.id.iNotifications.toString()

            )
            .hide(NotificationsFragment())
            .commitNow()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer, MapFragment(),
                R.id.iMap.toString()

            )
            .hide(MapFragment())
            .commitNow()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer, RouteFragment(),
                R.id.iRoute.toString()

            )
            .commitNow()
        lastActiveFragmentTag = R.id.iRoute.toString()
    }

    private fun setUpListeners() {
        bnvTabs.apply {
            setOnNavigationItemSelectedListener {
                showFragment(it.itemId)
                if (it.itemId == R.id.iNotifications) {
                    countUnreadNotifications = 0
                    removeBadge(R.id.iNotifications)

                }
                return@setOnNavigationItemSelectedListener true
            }
        }

    }

    private fun showFragment(tag: Int) {
        val fragment = supportFragmentManager.findFragmentByTag(tag.toString()) ?: when (tag) {
            R.id.iRoute -> {
                RouteFragment()
            }
            R.id.iMap -> {
                MapFragment()
            }
            R.id.iNotifications -> {
                NotificationsFragment()
            }
            R.id.iProfile -> {
                ProfileFragment()
            }
            else -> {
                null
            }
        }

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()

            if (lastActiveFragmentTag.isNotEmpty()) {
                val lastFragment = supportFragmentManager.findFragmentByTag(lastActiveFragmentTag)
                if (lastFragment != null) {
                    transaction.hide(lastFragment)
                }
            }

            if (!(fragment as Fragment).isAdded) {
                transaction.add(R.id.fragmentContainer, fragment, tag.toString())
            } else {
                transaction.show(fragment)
            }
            transaction.commit()
            lastActiveFragmentTag = tag.toString()
        }
    }


    private fun showNotificationBadge() {
        bnvTabs.getOrCreateBadge(R.id.iNotifications).apply {
            backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.colorError)
            badgeTextColor = ContextCompat.getColor(this@MainActivity, R.color.colorOnError)
            number = countUnreadNotifications
        }
    }





}