package io.polydev.public_transport.di.components

import dagger.Subcomponent
import io.polydev.public_transport.di.modules.ViewModelModule
import io.polydev.public_transport.di.scope.RouteScope
import io.polydev.public_transport.screens.main.route.RouteFragment


@RouteScope
@Subcomponent(modules = [ViewModelModule::class])
interface RouteComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RouteComponent
    }

    fun inject(routeFragment: RouteFragment)

}