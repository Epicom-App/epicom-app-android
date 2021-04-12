package org.ebolapp.features.permissions.usecases

import org.ebolapp.features.permissions.controller.PermissionController

class InitializePermissioHandlingUseCase internal constructor(
    private val permissionController: PermissionController
) {
    operator fun invoke() {
        permissionController.init()
    }
}