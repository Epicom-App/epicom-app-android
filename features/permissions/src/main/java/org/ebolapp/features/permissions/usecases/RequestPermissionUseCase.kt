package org.ebolapp.features.permissions.usecases

import org.ebolapp.features.permissions.controller.PermissionController
import org.ebolapp.features.permissions.models.Permission

class RequestPermissionUseCase(
    private val permissionController: PermissionController
) {
    /**
     *
     * The method prompts user to allow the given permissions.
     * 3 steps available:
     *
     * 1. Legal terms consent
     * 2. Usage description (optional) API >= 30
     * 3. System dialog or transition to Settings
     *
     * @param permissions the list of required permissions to ask
     * @param force the parameter whether the permissions should be asked anyway
     * even is user declined the legal terms consent (1 step)
     *
     */
    suspend operator fun invoke(permissions: Set<Permission>, force: Boolean) {
        permissionController.requestPermissions(permissions, force = force)
    }
}