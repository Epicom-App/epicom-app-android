package org.ebolapp.features.geotracking.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.UserLocation
import org.ebolapp.features.visits.usecases.locations.SaveUserLocationsUseCase
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

@OptIn(KoinApiExtension::class)
class LocationUpdatesBroadcastReceiver : BroadcastReceiver(), Logging, KoinComponent {

    private val TAG = Filter.BACKGROUND_JOB_LOG

    private val addUserLocationsUseCase by inject<SaveUserLocationsUseCase>()

    override fun onReceive(context: Context, intent: Intent?) {
        debug("LocationUpdatesBroadcastReceiver START", TAG)
        when (intent?.action) {
            ACTION_PROCESS_UPDATES -> {
                val result = LocationResult.extractResult(intent) ?: return
                debug("LocationUpdatesBroadcastReceiver SUCCESS: ${result.locations.toUserLocations()}", TAG)
                runBlocking {
                    withContext(Dispatchers.IO) {
                        addUserLocationsUseCase(result.locations.toUserLocations())
                    }
                }
            }
        }
    }

    companion object {
        const val ACTION_PROCESS_UPDATES =
            "org.ebolapp.features.geotracking.locationupdatespendingintent.action" + ".PROCESS_UPDATES"
    }

}

private fun List<Location>.toUserLocations(): List<UserLocation> =
    with(mutableListOf<UserLocation>()) {
        this@toUserLocations.forEach { location ->
            add(UserLocation(
                position = Position(
                    lat = location.latitude,
                    lon = location.longitude
                ),
                timestampSec = TimeUnit.MILLISECONDS.toSeconds(location.time)
            ))
        }
        this
    }
