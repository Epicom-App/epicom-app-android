package org.ebolapp.ui.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.ebolapp.presentation.webview.DefaultWebViewModel
import org.ebolapp.presentation.webview.StaticPageWebViewModel
import org.ebolapp.presentation.webview.WebViewViewModel
import org.ebolapp.ui.map.views.showErrorDialog
import org.ebolapp.ui.webview.databinding.UiWebviewFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class DefaultWebViewFragment(url: String) : WebViewFragment(url) {
    override val viewModel: WebViewViewModel by viewModel<DefaultWebViewModel>()
}

@FlowPreview
@ExperimentalCoroutinesApi
class StaticPageWebViewFragment(url: String) : WebViewFragment(url) {
    override val viewModel: WebViewViewModel by viewModel<StaticPageWebViewModel>()
}

@FlowPreview
@ExperimentalCoroutinesApi
open class WebViewFragment(
    private val url: String,
): Fragment(R.layout.ui_webview_fragment) {

    private var binding: UiWebviewFragmentBinding? = null
    open val viewModel: WebViewViewModel
        get() = throw Exception("Use default or static webView activity instead")

    private val loadURLTrigger = MutableSharedFlow<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UiWebviewFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewModel()
        loadURL()
    }

    private fun configureViewModel() {
        val binding = binding ?: return
        val output = viewModel.bind(
            WebViewViewModel.Input(
            webView = flowOf(binding.webview)
        ))
        // On start loading
        output.onStart.onEach {
            binding.loadingIndicator.show()
        }.launchIn(lifecycleScope)
        // On finish loading
        output.onFinish.onEach {
            binding.loadingIndicator.hide()
        }.launchIn(lifecycleScope)
        // On fail loading
        output.onFail.onEach {
            showErrorAlert()
        }.launchIn(lifecycleScope)
        // On load URL trigger
        loadURLTrigger.onEach {
            output.loadURLTrigger.emit(it)
        }.launchIn(lifecycleScope)
    }

    private fun loadURL() {
        lifecycleScope.launch {
            loadURLTrigger.emit(url)
        }
    }

    private fun showErrorAlert() {
        context?.showErrorDialog(
            titleResId = R.string.error_title,
            messageResId = R.string.error_default_message,
            onNegative = this::loadURL
        )
    }
}