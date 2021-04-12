package org.ebolapp.ui.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.ebolapp.ui.webview.databinding.UiWebviewActivityBinding

@FlowPreview
@ExperimentalCoroutinesApi
open class WebViewActivity : AppCompatActivity() {

    data class Dependencies(
        val url: String,
        val title: String? = null,
        val isStatic: Boolean = false
    )

    companion object {
        private const val TITLE_PARAMETER = "TITLE_PARAMETER"
        private const val URL_PARAMETER = "URL_PARAMETER"
        private const val IS_STATIC_PARAMETER = "IS_STATIC_PARAMETER"

        fun start(context: Context, dependencies: Dependencies) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(URL_PARAMETER, dependencies.url)
            intent.putExtra(TITLE_PARAMETER, dependencies.title)
            intent.putExtra(IS_STATIC_PARAMETER, dependencies.isStatic)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: UiWebviewActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        configureRootFragment()
        super.onCreate(savedInstanceState)
        configureView()
        configureNavigation()
    }

    private fun configureView() {
        binding = UiWebviewActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentClass = if (intent.getBooleanExtra(IS_STATIC_PARAMETER, false))
            StaticPageWebViewFragment::class.java else DefaultWebViewFragment::class.java
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragmentClass, null)
            .commit()
    }

    private fun configureRootFragment() {
        intent.getStringExtra(URL_PARAMETER)?.let { url ->
            supportFragmentManager.fragmentFactory = WebViewFragmentFactory(url)
        }
    }

    private fun configureNavigation() {
        binding.toolbarTitle.text = intent.getStringExtra(TITLE_PARAMETER)
        binding.toolbar.setNavigationIcon(R.drawable.ui_common_ic_close)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}