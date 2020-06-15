package io.polydev.public_transport

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.github.nkzawa.engineio.client.transports.WebSocket
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import io.polydev.public_transport.di.ComponentManager
import io.polydev.public_transport.utils.AppConstants
import io.polydev.public_transport.utils.data.PreferencesManager
import io.polydev.public_transport.utils.sockets.SocketManager
import io.realm.Realm
import io.realm.RealmConfiguration
import java.lang.Exception


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpRealm()
        setUpPreferencesManager()
        setUpSocket()
        ComponentManager.initAppComponent()
    }

    private fun setUpPreferencesManager() {
        PreferencesManager.setContext(this)
    }


    private fun setUpSocket() {
        try {
            SocketManager.setSocket(IO.socket(AppConstants.SOCKET_URL)!!)
        } catch (ex: Exception) {
            Log.d(AppConstants.LOG_TAG_ERROR,"setUpSocket " + ex.message)
            Toast.makeText(this,"Не удалось подключиться к серверу", Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun setUpRealm() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)

    }
}