package io.polydev.public_transport.base

abstract class BaseRepository<VM: BaseViewModel> {

    var viewModel: VM? = null

    fun attachViewModel(viewModel: VM) {
        this.viewModel = viewModel
    }

    fun detachViewModel() {
        viewModel = null
    }

}