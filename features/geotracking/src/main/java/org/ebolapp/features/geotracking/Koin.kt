package org.ebolapp.features.geotracking

import org.ebolapp.features.geotracking.location.LocationService
import org.ebolapp.features.geotracking.location.LocationServiceImpl
import org.ebolapp.features.geotracking.usecases.GetCurrentPositionUseCase
import org.ebolapp.features.geotracking.usecases.GetCurrentPositionUseCaseImpl
import org.koin.dsl.module

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
val geoTrackingModule = module {
    factory<LocationService> {
        LocationServiceImpl(
            context = get(),
            hasPermissionUseCase = get()
        )
    }
    factory<GetCurrentPositionUseCase> {
        GetCurrentPositionUseCaseImpl(locationService = get())
    }
}
