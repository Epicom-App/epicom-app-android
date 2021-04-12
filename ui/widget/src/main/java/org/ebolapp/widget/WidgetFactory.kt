package org.ebolapp.widget

import android.content.Context
import org.ebolapp.features.permissions.usecases.ObservePermissionsChangeUseCase
import org.ebolapp.widget.services.ObserveNetworkStateChangesUseCase
import org.ebolapp.widget.services.ObserveNetworkStateChangesUseCaseImpl
import org.ebolapp.widget.services.StartWidgetUpdaterUseCase
import org.ebolapp.widget.services.StartWidgetUpdaterUseCaseImpl

class WidgetFactory(
    private val context: Context,
    private val observePermissionsChangeUseCase: ObservePermissionsChangeUseCase
) {

    fun createConnectivityObserver(): ObserveNetworkStateChangesUseCase =
        ObserveNetworkStateChangesUseCaseImpl(context)

    fun createStartWidgetUpdaterUseCase(): StartWidgetUpdaterUseCase =
        StartWidgetUpdaterUseCaseImpl(
            context = context,
            observeNetworkStateChangesUseCase = createConnectivityObserver(),
            observePermissionsChangeUseCase = observePermissionsChangeUseCase
        )

}