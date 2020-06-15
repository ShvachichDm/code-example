package io.polydev.public_transport.di.components

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {

    fun routeComponent(): RouteComponent.Factory
}
