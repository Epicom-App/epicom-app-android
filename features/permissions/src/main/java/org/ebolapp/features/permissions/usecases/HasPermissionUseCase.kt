package org.ebolapp.features.permissions.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.ebolapp.features.permissions.controller.AndroidPermissionController
import org.ebolapp.features.permissions.controller.PermissionController
import org.ebolapp.features.permissions.models.Permission

class HasPermissionUseCase(
    private val permissionsController: PermissionController
) {
    operator fun invoke(permission: Permission): Flow<Boolean> {
        return permissionsController.availablePermissions
            .map { it.contains(permission) }
            .distinctUntilChanged()
    }
}