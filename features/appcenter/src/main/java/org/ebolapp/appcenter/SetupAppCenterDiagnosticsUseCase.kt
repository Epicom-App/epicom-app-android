package org.ebolapp.appcenter

import android.app.Application
import android.content.Context
import android.util.Log
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute

interface SetupAppCenterDiagnosticsUseCase {
    operator fun invoke(isDebug: Boolean)
}

internal class SetupAppCenterDiagnosticsUseCaseImpl (
    private val context: Context,
    private val appCenterSecret: String
): SetupAppCenterDiagnosticsUseCase {

    override fun invoke(isDebug: Boolean) {
        if (isDebug) enable() else disable()
    }

    private fun enable() {
        AppCenter.setLogLevel(Log.INFO)

        Distribute.setEnabledForDebuggableBuild(true)
        Distribute.setEnabled(true)

        AppCenter.start(
            context.applicationContext as Application,
            appCenterSecret,
            Distribute::class.java,
            Crashes::class.java
        )

        AppCenter.setEnabled(true)
    }

    private fun disable() {
        AppCenter.setEnabled(false)
    }
}