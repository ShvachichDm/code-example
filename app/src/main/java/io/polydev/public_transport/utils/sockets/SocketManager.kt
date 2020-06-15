package io.polydev.public_transport.utils.sockets

import android.util.Log
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import io.polydev.public_transport.utils.AppConstants
import io.polydev.public_transport.utils.sockets.events.*
import org.json.JSONObject

object SocketManager {

    private var socket: Socket? = null
    private var generalListener: GeneralSocketListener? = null
    private var routeListener: RouteSocketListener? = null
    private var mapListener: MapSocketListener? = null
    private var notificationsListener: NotificationsSocketListener? = null
    private var profileListener: ProfileSocketListener? = null

    fun setSocket(socket: Socket?) {

        this.socket = socket?.apply {
//            on("connect") {
//                Log.d(AppConstants.LOG_TAG_2, "connect")
//                this@SocketManager.generalListener?.onConnect()
//            }
//            on("disconnect") {
//                Log.d(AppConstants.LOG_TAG_2, "disconnect")
//                this@SocketManager.generalListener?.onDisconnect()
//            }
//            on("authenticated") {
//                Log.d(AppConstants.LOG_TAG_2, "authenticated")
//                this@SocketManager.generalListener?.onAuthenticated()
//            }
//            on("unauthenticated") {
//                Log.d(AppConstants.LOG_TAG_2, "unauthenticated")
//                this@SocketManager.generalListener?.onUnAuthenticated()
//            }
//            on("error") {
//                Log.d(AppConstants.LOG_TAG_2, "error")
//                this@SocketManager.generalListener?.onError()
//            }
//            on("notification") {
//                Log.d(AppConstants.LOG_TAG_2, "notification\n json:${it[0]}")
//                try {
//                    val jsonObject = Gson().fromJson<NotificationEvent>(
//                        it[0].toString(),
//                        NotificationEvent::class.java
//                    )
//                    this@SocketManager.generalListener?.onNotification(jsonObject)
//                    this@SocketManager.notificationsListener?.onNotification(jsonObject)
//                } catch (e: Exception) {
//                    Log.d(AppConstants.LOG_TAG_ERROR, "notification " +e.message.toString())
//                    e.printStackTrace()
//                }
//
//            }
////            on("startCircle") {
////                Log.d(AppConstants.LOG_TAG_2, "startCircle\njson: ${it[0]}")
////                try {
////                    val type = object : TypeToken<List<CircleJsonObject>>() {}.type
////                    val jsonObject = Gson().fromJson<List<CircleJsonObject>>(
////                        it[0].toString(),
////                        type
////                    )
////                    this@SocketManager.routeListener?.onStartCircle(jsonObject)
////                } catch (e: Exception) {
////                    Log.d(AppConstants.LOG_TAG_ERROR, "startCircle " +e.message.toString())
////                    e.printStackTrace()
////                }
////
////
////            }
//            on("startCircle") {
//                Log.d(AppConstants.LOG_TAG_2, "startCircle\njson: ${it[0]}")
//                try {
//                    val jsonObject = Gson().fromJson<CircleEvent>(
//                        it[0].toString(),
//                        CircleEvent::class.java
//                    )
//                    this@SocketManager.routeListener?.onStartCircle(jsonObject)
//                } catch (e: Exception) {
//                    Log.d(AppConstants.LOG_TAG_ERROR, "startCircle " +e.message.toString())
//                    e.printStackTrace()
//                }
//
//            }
//            on("finishCircle") {
//                Log.d(AppConstants.LOG_TAG_2, "finishCircle\njson: ${it[0]}")
//                try {
//                    val jsonObject = Gson().fromJson<CircleEvent>(
//                        it[0].toString(),
//                        CircleEvent::class.java
//                    )
//                    this@SocketManager.routeListener?.onFinishCircle(jsonObject)
//                } catch (e: Exception) {
//                    Log.d(AppConstants.LOG_TAG_ERROR, "finishCircle " + e.message.toString())
//                    e.printStackTrace()
//                }
//
//            }
////            on("busData") {
////                Log.d(AppConstants.LOG_TAG_2, "busData\njson: ${it[0]}")
////                try {
////                    val jsonObject = Gson().fromJson<BusDataEvent>(it[0].toString(), BusDataEvent::class.java)
////                    this@SocketManager.routeListener?.onBusData(jsonObject)
////                } catch (e: Exception) {
////                    Log.d(AppConstants.LOG_TAG_ERROR, "busData " +e.message.toString())
////                    e.printStackTrace()
////                }
////
////
////            }
////
////            on("geoData") {
////                Log.d(AppConstants.LOG_TAG_2, "geoData\njson: ${it[0]}")
////                try {
////                    val type = object : TypeToken<List<GeoDataJsonObject>>() {}.type
////
////                    val jsonObject = Gson().fromJson<List<GeoDataJsonObject>>(it[0].toString(), type)
////                    this@SocketManager.mapListener?.onGeoData(jsonObject)
////                } catch (e: Exception) {
////                    Log.d(AppConstants.LOG_TAG_ERROR, "geoData " +e.message.toString())
////                    e.printStackTrace()
////                }
////
////
////            }
//
//            on("busData") {
//                Log.d(AppConstants.LOG_TAG_2, "busData\njson: ${it[0]}")
//                try {
//                    val jsonObject = Gson().fromJson<BusDataEvent>(it[0].toString(), BusDataEvent::class.java)
//                    this@SocketManager.routeListener?.onBusData(jsonObject)
//                    this@SocketManager.mapListener?.onBusData(jsonObject)
//                } catch (e: Exception) {
//                    Log.d(AppConstants.LOG_TAG_ERROR, "busData " +e.message.toString())
//                    e.printStackTrace()
//                }
//            }
//
//
//            on("sendMapEventResult") {
//                Log.d(AppConstants.LOG_TAG_2, "sendMapEventResult\njson: ${it[0]}")
//                try {
//                    val jsonObject =
//                        Gson().fromJson<SendMapEventResult>(it[0].toString(), SendMapEventResult::class.java)
//                    this@SocketManager.mapListener?.onSendMapEventResult(jsonObject)
//                } catch (e: Exception) {
//                    Log.d(AppConstants.LOG_TAG_ERROR, "sendMapEventResult " + e.message.toString())
//                    e.printStackTrace()
//                }
//            }
//
//            on("changeRoute") {
//                Log.d(AppConstants.LOG_TAG_2, "changeRoute\njson: ${it[0]}")
//                try {
//                    val jsonObject = Gson().fromJson<ChangeRouteEvent>(
//                        it[0].toString(),
//                        ChangeRouteEvent::class.java
//                    )
//                    this@SocketManager.mapListener?.onChangeRoute(jsonObject as ChangeRouteEvent)
//                } catch (e: Exception) {
//                    Log.d(AppConstants.LOG_TAG_ERROR, "changeRoute " + e.message.toString())
//                    e.printStackTrace()
//                }
//            }
//

        }

    }

    fun setRouteSocketListener(listener: RouteSocketListener?) {
        routeListener = listener
    }

    fun setMapSocketListener(listener: MapSocketListener?) {
        mapListener = listener
    }

    fun setNotificationsSocketListener(listener: NotificationsSocketListener?) {
        notificationsListener = listener
    }

    fun setProfileSocketListener(listener: ProfileSocketListener?) {
        profileListener = listener
    }

    fun setGeneralSocketListener(listener: GeneralSocketListener?) {
        generalListener = listener
    }

    fun authenticate(token: String) {
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        socket!!.emit("authenticate", jsonObject)
    }

    fun sendMapEvent(event: String) {
        val jsonObject = JSONObject()
        jsonObject.put("type", event)
        socket!!.emit("sendMapEvent", jsonObject)
    }

    fun sendGpsData(lat: String, lon: String) {
        val jsonObject = JSONObject()
        jsonObject.put("lat",lat)
        jsonObject.put("lon",lon)
        socket!!.emit("gpsData", jsonObject)
    }

    fun connect() {
        socket!!.connect()
    }

    fun socketConnected(): Boolean {
        return socket!!.connected()
    }

    fun disconnect() {
        socket!!.disconnect()
    }

    interface RouteSocketListener {
        fun onBusData(busData: BusDataEvent)
        fun onStartCircle(circleEvent: CircleEvent)
        fun onFinishCircle(circleEvent: CircleEvent)
    }

    interface MapSocketListener {
        fun onBusData(busData: BusDataEvent)
        fun onSendMapEventResult(sendMapEventResult: SendMapEventResult)
        fun onChangeRoute(changeRouteEvent: ChangeRouteEvent)
    }

    interface NotificationsSocketListener {
        fun onNotification(notification: NotificationEvent)
    }

    interface GeneralSocketListener {
        fun onConnect()
        fun onDisconnect()
        fun onAuthenticated()
        fun onUnAuthenticated()
        fun onError()
        fun onNotification(notificationEvent: NotificationEvent)
    }

    interface ProfileSocketListener {

    }

}