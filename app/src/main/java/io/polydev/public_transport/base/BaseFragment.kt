package io.polydev.public_transport.base


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.polydev.public_transport.base.utils.observe
import io.polydev.public_transport.base.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

/**
 * Created by AhmedEltaher
 */


abstract class BaseFragment<VM: BaseViewModel>(private val layoutId: Int, private val viewModelClass: Class<VM>) : Fragment(), CoroutineScope{

    private val job = Job()
    override val coroutineContext = job + Dispatchers.Main

    abstract fun observeViewModel()
    abstract fun setUpUI(view: View)
    abstract fun injectComponent()
    abstract fun clearComponent()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: VM


    override fun onAttach(context: Context) {
        injectComponent()
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass]
        observeViewModel()
        observeToastMessage()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        clearComponent()
    }

    private fun observeToastMessage() {
        observe(viewModel.toastMessage) {
            it.getContentIfNotHandled()?.let { message ->
                context.toast(message)
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)
        setUpUI(view)
        return view
    }

}