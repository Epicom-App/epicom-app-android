package org.ebolapp.widget.services

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.ebolapp.features.permissions.models.Permission
import org.ebolapp.features.permissions.usecases.ObservePermissionsChangeUseCase
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.widget.EbolaAppWidgetUpdateService
import kotlin.coroutines.CoroutineContext


/**
 * When started listens for specific application state changes,
 * and then perform action to update widget
 */
interface StartWidgetUpdaterUseCase {
    operator fun invoke()
}

internal class StartWidgetUpdaterUseCaseImpl(
    private val context: Context,
    private val observeNetworkStateChangesUseCase: ObserveNetworkStateChangesUseCase,
    private val observePermissionsChangeUseCase: ObservePermissionsChangeUseCase
) : StartWidgetUpdaterUseCase, CoroutineScope, Logging {

    override val coroutineContext: CoroutineContext = Job()

    override fun invoke() {

        updateWidget()

        observeNetworkStateChangesUseCase()
            .onEach {
                debug("Connectivity changed $it")
                updateWidget()
            }.launchIn(this)
        observePermissionsChangeUseCase(
            listOf(
                Permission.FOREGROUND_LOCATION,
                Permission.BACKGROUND_LOCATION
            )
        ).onEach {
            debug("Permissions changed $it")
            updateWidget()
        }.launchIn(this)
    }

    private fun updateWidget() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            context.startService(Intent(context, EbolaAppWidgetUpdateService::class.java))
        } else {
            context.startForegroundService(Intent(context, EbolaAppWidgetUpdateService::class.java))
        }
    }
}