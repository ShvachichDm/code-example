package io.polydev.public_transport.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.polydev.public_transport.base.utils.observe
import io.polydev.public_transport.base.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

abstract class BaseActivity<VM: BaseViewModel>(private val layoutId: Int, private val viewModelClass: Class<VM>) : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + Dispatchers.Main
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: VM

    abstract fun observeViewModel()
    abstract fun setUpUI()
    abstract fun injectComponent()
    abstract fun clearComponent()


    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass]
        setContentView(layoutId)
        setUpUI()
        observeViewModel()
        observeToastMessage()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearComponent()
        job.cancel()
    }

    private fun observeToastMessage() {
        observe(viewModel.toastMessage) {
            it.getContentIfNotHandled()?.let { message ->
                toast(message)
            }
        }
    }

}



