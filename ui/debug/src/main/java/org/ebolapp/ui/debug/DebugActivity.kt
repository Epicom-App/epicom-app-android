package org.ebolapp.ui.debug

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.ebolapp.ui.debug.databinding.UiDebugActivityBinding

class DebugActivity : AppCompatActivity() {


    private lateinit var binding: UiDebugActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.DayNight_Theme_EbolaApp)
        binding = UiDebugActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}