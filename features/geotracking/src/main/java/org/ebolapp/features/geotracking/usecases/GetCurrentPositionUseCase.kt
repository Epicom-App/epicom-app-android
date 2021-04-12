package org.ebolapp.features.geotracking.usecases

import android.location.Location
import org.ebolapp.features.geotracking.location.LocationService

interface GetCurrentPositionUseCase {
    suspend operator fun invoke() : Location?
}

internal class GetCurrentPositionUseCaseImpl(
    private val locationService: LocationService
) : GetCurrentPositionUseCase {
    override suspend operator fun invoke(): Location? {
        return locationService.getLastKnownLocation()
    }
}
