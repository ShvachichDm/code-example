package io.polydev.public_transport.utils.data

import io.polydev.public_transport.data_models.*
import io.realm.Realm
import io.realm.RealmObject

object RealmManager {

    fun addNotification(notification: NotificationData) {
        Realm.getDefaultInstance().use { instance ->
            instance.executeTransaction {
                it.copyToRealm(notification)
            }
            instance.close()
        }
    }

    fun getNotifications(): ArrayList<NotificationData>? {
        var notifications: ArrayList<NotificationData>? = null
        Realm.getDefaultInstance().use { instance ->
            val foundData = instance.where(NotificationData::class.java).findAll()
            notifications = if (foundData != null) {
                ArrayList(instance.copyFromRealm(foundData))
            } else {
                null
            }
            instance.close()
        }
        return notifications
    }

    fun updateNotifications(notifications: ArrayList<NotificationData>) {
        Realm.getDefaultInstance().use {instance ->
            instance.executeTransaction {realm ->
                realm.delete(NotificationData::class.java)
                notifications.forEach {
                    realm.copyToRealm(it)
                }
            }
            instance.close()
        }
    }

    fun clearData() {
        Realm.getDefaultInstance().use {instance ->
            instance.executeTransaction {
                it.deleteAll()
            }
            instance.close()

        }
    }

    fun updateRingTimes(tripTimes: ArrayList<TripTimeData>) {
        Realm.getDefaultInstance().use {instance ->
            instance.executeTransaction {realm ->
                realm.delete(TripTimeData::class.java)
                tripTimes.forEach {
                    realm.copyToRealm(it)
                }
            }
            instance.close()
        }
    }

    fun getRingTimes(): ArrayList<TripTimeData>? {
        var tripTimes: ArrayList<TripTimeData>? = null
        Realm.getDefaultInstance().use { instance ->
            val results = instance.where(TripTimeData::class.java).findAll()
            tripTimes = if (results != null) {
                ArrayList(instance.copyFromRealm(results))
            } else {
                null
            }
            instance.close()
        }
        return tripTimes
    }

    fun addRouteHistory(routeHistory: ArrayList<RouteHistoryData>) {
        Realm.getDefaultInstance().use {instance ->
            instance.executeTransaction {realm ->
                routeHistory.forEach {
                    realm.copyToRealm(it)
                }
            }
            instance.close()
        }
    }
    fun updateRouteHistory(routeHistory: ArrayList<RouteHistoryData>) {
        Realm.getDefaultInstance().use {instance ->
            instance.executeTransaction {realm ->
                realm.delete(RouteHistoryData::class.java)
                routeHistory.forEach {
                    realm.copyToRealm(it)
                }
            }
            instance.close()
        }
    }

    fun getRouteHistory(): ArrayList<RouteHistoryData>? {
        var routeHistory: ArrayList<RouteHistoryData>? = null
        Realm.getDefaultInstance().use { instance ->
            val foundData = instance.where(RouteHistoryData::class.java).findAll()
            routeHistory = if (foundData != null) {
                ArrayList(instance.copyFromRealm(foundData))
            } else {
                null
            }
            instance.close()
        }
        return routeHistory
    }

    inline fun<reified T: RealmObject> getData(): T? {
        var data: T? = null
        Realm.getDefaultInstance().use { instance ->
            val foundData = instance.where(T::class.java).findFirst()
            data = if (foundData != null) {
                instance.copyFromRealm(foundData)
            } else {
                null
            }
            instance.close()
        }
        return data
    }

    inline fun<reified T: RealmObject> updateData(data: T) {
        Realm.getDefaultInstance().use { instance ->
            instance.executeTransaction {
                it.delete(T::class.java)
                it.copyToRealm(data)
            }
            instance.close()
        }
    }


}