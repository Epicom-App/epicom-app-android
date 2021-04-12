package org.ebolapp.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import org.ebolapp.ui.settings.databinding.UiSettingsActivityBinding
import org.ebolapp.ui.settings.utils.SettingsFragmentFactory
import org.ebolapp.ui.settings.utils.SettingsScreenNavigator
import org.koin.android.ext.android.get

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: UiSettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        configureRootFragment()
        super.onCreate(savedInstanceState)
        configureView()
        configureToolbar()
    }

    private fun configureView() {
        binding = UiSettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, SettingsFragment::class.java, null)
            .commit()
    }

    private fun configureToolbar() {
        binding.toolbarTitle.text = getString(R.string.settings_title)
        binding.toolbar.setNavigationIcon(R.drawable.ui_common_ic_close)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun configureRootFragment() {
        val screenNavigator = SettingsScreenNavigator(this, get(), get())
        supportFragmentManager.fragmentFactory = SettingsFragmentFactory(screenNavigator)
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
