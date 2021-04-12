package org.ebolapp.features.permissions.controller

import kotlinx.coroutines.flow.Flow
import org.ebolapp.features.permissions.models.Permission

interface PermissionController {
    fun init()
    val availablePermissions: Flow<Set<Permission>>
    suspend fun requestPermissions(vararg permissions: Permission)
}