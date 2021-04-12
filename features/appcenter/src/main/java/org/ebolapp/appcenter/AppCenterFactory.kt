package org.ebolapp.appcenter

import android.content.Context

class AppCenterFactory(
    private val context: Context,
    private val appCenterSecret: String
) {
    fun createSetupAppCenterUseCase() : SetupAppCenterDiagnosticsUseCase =
        SetupAppCenterDiagnosticsUseCaseImpl(context, appCenterSecret)
}