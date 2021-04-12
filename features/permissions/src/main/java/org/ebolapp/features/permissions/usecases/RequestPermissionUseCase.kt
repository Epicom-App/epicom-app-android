package org.ebolapp.features.permissions.usecases

import org.ebolapp.features.permissions.controller.PermissionController
import org.ebolapp.features.permissions.models.Permission

class RequestPermissionUseCase(
    private val permissionController: PermissionController
) {
    suspend operator fun invoke(vararg permissions: Permission) {
        permissionController.requestPermissions(*permissions)
    }
}