package io.polydev.public_transport.di

import io.polydev.public_transport.di.components.AppComponent
import io.polydev.public_transport.di.components.DaggerAppComponent
import io.polydev.public_transport.di.components.RouteComponent

object ComponentManager {
    private lateinit var appComponent: AppComponent
    private var routeComponent: RouteComponent? = null

    fun getAppComponent(): AppComponent = appComponent

    fun initAppComponent() {
        appComponent = DaggerAppComponent.create()
    }

    fun getRouteComponent(): RouteComponent {
        if(routeComponent == null) {
            routeComponent = appComponent.routeComponent().create()
        }
        return routeComponent!!
    }

    fun clearRouteComponent() {
        routeComponent = null
    }
}