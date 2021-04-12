package org.ebolapp.features.permissions.usecases

import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.ebolapp.features.permissions.controller.toAndroidPermission
import org.ebolapp.features.permissions.models.Permission
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug

interface ObservePermissionsChangeUseCase {
    operator fun invoke(permissions: List<Permission>): Flow<List<Permission>>
}

internal class ObservePermissionsChangeUseCaseImpl(
    private val context: Context
): ObservePermissionsChangeUseCase, Logging {

    override fun invoke(permissions: List<Permission>): Flow<List<Permission>> = flow {

        debug("Init permissions change")

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) return@flow

        var activePermissions: List<Permission> = permissions

        while (currentCoroutineContext().isActive) {

            delay(5000)

            val previousActivePermissions = activePermissions
            activePermissions = permissions.mapNotNull { permission ->
                val androidPermissions = permission.toAndroidPermission()
                val androidActivePermissions = androidPermissions.filter {
                    context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
                }
                if (androidPermissions == androidActivePermissions) permission else null
            }

            debug("prev $previousActivePermissions")
            debug("current $activePermissions")

            if (previousActivePermissions != activePermissions) {
                emit(activePermissions)
                debug("Permissions changed $activePermissions")
            } else {
                debug("Permissions unchanged $activePermissions")
            }
        }
    }
}