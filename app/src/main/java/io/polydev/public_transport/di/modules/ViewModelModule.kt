package io.polydev.public_transport.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.polydev.public_transport.base.utils.ViewModelFactory
import io.polydev.public_transport.di.keys.ViewModelKey
import io.polydev.public_transport.di.scope.RouteScope
import io.polydev.public_transport.screens.main.route.RouteViewModel


@Module
abstract class ViewModelModule {

    @RouteScope
    @Binds
    @IntoMap
    @ViewModelKey(RouteViewModel::class)
    abstract fun bindRouteViewModel(viewModel: RouteViewModel): ViewModel

    @RouteScope
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
