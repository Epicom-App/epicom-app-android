package org.ebolapp.ui.legend

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.ebolapp.ui.legend.databinding.UiLegendActivityBinding

class LegendActivity : AppCompatActivity() {

    private lateinit var binding: UiLegendActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureView()
        configureToolbar()
    }

    private fun configureView() {
        binding = UiLegendActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.legendView.setTitle(getString(R.string.legend_navigation_title))
    }

    private fun configureToolbar() {
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