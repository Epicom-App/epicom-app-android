package org.ebolapp.ui.webview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class WebViewFragmentFactory(private val url: String) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            DefaultWebViewFragment::class.java.name -> DefaultWebViewFragment(url)
            StaticPageWebViewFragment::class.java.name -> StaticPageWebViewFragment(url)
            else -> super.instantiate(classLoader, className)
        }
    }
}