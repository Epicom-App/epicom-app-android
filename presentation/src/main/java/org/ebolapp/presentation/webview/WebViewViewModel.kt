package org.ebolapp.presentation.webview

import android.annotation.SuppressLint
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.ebolapp.features.staticPages.usecases.GetStaticPageUseCase
import java.lang.Exception

@FlowPreview
@ExperimentalCoroutinesApi
class StaticPageWebViewModel(
    private val getStaticPageUseCase: GetStaticPageUseCase
): WebViewViewModel()  {

    override suspend fun load(url: String, inWebView: WebView) {
        didStartStream.emit(Unit)
        try {
            val html = getStaticPageUseCase.invoke(url)
            val meta = "<meta name=\"viewport\" content=\"initial-scale=1.0\" />"
            inWebView.loadDataWithBaseURL(null,meta + html, "text/html; charset=utf-8", "UTF-8", null)
            didLoadStream.take(1).collect()
        } catch (e: Throwable) {
            didLoadStream.emit(Unit)
            didFailStream.emit(Unit)
        }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
class DefaultWebViewModel: WebViewViewModel() {

    override suspend fun load(url: String, inWebView: WebView) {
        didStartStream.emit(Unit)
        inWebView.loadUrl(url)
        didLoadStream.take(1).collect()
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
open class WebViewViewModel : ViewModel() {

    data class Input(
        val webView: Flow<WebView>
    )
    data class Output(
        val onStart: Flow<Unit>,
        val onFinish: Flow<Unit>,
        val onFail: Flow<Unit>,
        val loadURLTrigger: MutableSharedFlow<String>
    )

    private val loadURLTrigger: MutableSharedFlow<String> = MutableSharedFlow()

    fun bind(input: Input) : Output {
        val didFinishLoading = combine(loadURLTrigger, input.webView) { url, webView ->
            configure(webView)
            load(url, webView)
        }
        return Output(
            onStart = didStartStream,
            onFinish = didFinishLoading,
            onFail = didFailStream,
            loadURLTrigger = loadURLTrigger
        )
    }

    // Override in subclass
    open suspend fun load(url: String, inWebView: WebView) = Unit

    // Configure webView
    val didStartStream = MutableSharedFlow<Unit>()
    val didLoadStream = MutableSharedFlow<Unit>()
    val didFailStream = MutableSharedFlow<Unit>()

    @SuppressLint("SetJavaScriptEnabled")
    fun configure(webView: WebView) {
        webView.webViewClient = object : EpicomWebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewModelScope.launch {
                    didLoadStream.emit(Unit)
                }
            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                viewModelScope.launch {
                    didLoadStream.emit(Unit)
                    didFailStream.emit(Unit)
                }
            }
        }
        webView.settings.javaScriptEnabled = true
    }
}

