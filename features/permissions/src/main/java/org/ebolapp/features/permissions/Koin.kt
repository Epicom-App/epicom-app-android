package org.ebolapp.features.permissions

import androidx.core.app.ActivityCompat
import org.ebolapp.features.permissions.controller.AndroidPermissionController
import org.ebolapp.features.permissions.controller.PermissionController
import org.ebolapp.features.permissions.usecases.*
import org.koin.dsl.binds
import org.koin.dsl.module

val permissionsModule = module {
    factory {
        AvailabePermissionsUseCase(
            permissionController = get()
        )
    }

    factory {
        RequestPermissionUseCase(
            permissionController = get()
        )
    }

    factory {
        HasPermissionUseCase(
            permissionsController = get()
        )
    }
    factory {
        InitializePermissioHandlingUseCase(
            permissionController = get()
        )
    }
    factory<ObservePermissionsChangeUseCase>{
        ObservePermissionsChangeUseCaseImpl(context = get())
    }

    single { AndroidPermissionController(context = get(), resourceConfiguration = get()) } binds (arrayOf(
        ActivityCompat.OnRequestPermissionsResultCallback::class,
        PermissionController::class
    ))
}
