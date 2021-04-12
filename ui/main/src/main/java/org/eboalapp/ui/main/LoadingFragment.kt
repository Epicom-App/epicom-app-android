package org.eboalapp.ui.main


import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment

class LoadingFragment : Fragment(R.layout.ui_main_loading_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) (keyCode == KeyEvent.KEYCODE_BACK) else false
        }
    }

}