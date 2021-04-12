package org.ebolapp.features.permissions.usecases

import kotlinx.coroutines.flow.Flow
import org.ebolapp.features.permissions.controller.AndroidPermissionController
import org.ebolapp.features.permissions.controller.PermissionController
import org.ebolapp.features.permissions.models.Permission

class AvailabePermissionsUseCase(
    private val permissionController: PermissionController
) {
    operator fun invoke(): Flow<Set<Permission>> {
        return permissionController.availablePermissions
    }
}