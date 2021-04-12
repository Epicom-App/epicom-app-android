package org.ebolapp.presentation.widget

import android.content.Context
import android.location.Location
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.ebolapp.features.geotracking.usecases.GetCurrentPositionUseCase
import org.ebolapp.features.permissions.models.Permission
import org.ebolapp.features.permissions.usecases.HasPermissionUseCase
import org.ebolapp.features.regions.entities.MapRegionInfo
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.regions.usecases.GetMapRegionIdByPositionUseCase
import org.ebolapp.features.regions.usecases.GetMapRegionInfoUseCase
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter
import org.ebolapp.utils.DateUtils
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

class WidgetViewModel(
    private val getMapRegionByPositionUseCase: GetMapRegionIdByPositionUseCase,
    private val getMapRegionInfoUseCase: GetMapRegionInfoUseCase,
    private val getCurrentLocationUseCase: GetCurrentPositionUseCase,
    private val hasPermissionsUseCase: HasPermissionUseCase,
    private val cacheWidgetInfoUseCase: CacheWidgetInfoUseCase
) : Logging {

    private val TAG = Filter.BACKGROUND_JOB_LOG

    fun getWidgetInfo(): Flow<WidgetInfo> = flow {

        debug("getWidgetInfo START", TAG)

        emit(cacheWidgetInfoUseCase.getCached() ?: WidgetInfo.Loading)

        if (hasPermissionsUseCase(Permission.FOREGROUND_LOCATION).first()) {
            debug("getWidgetInfo - has foreground location permission - SUCCESS", TAG)

            val currentLocation: Location = getCurrentLocationUseCase() ?: run {
                emit(cacheWidgetInfoUseCase.getCached() ?: WidgetInfo.NoLocation)
                return@flow
            }
            debug("getWidgetInfo - current location $currentLocation", TAG)

            val currentRegionId: String
            val regionInfo: MapRegionInfo
            try {
                currentRegionId = getMapRegionByPositionUseCase(currentLocation.toPosition()) ?: run {
                    emit(WidgetInfo.NoCases)
                    return@flow
                }
                debug("getWidgetInfo - current regionId $currentRegionId", TAG)

                regionInfo =
                    getMapRegionInfoUseCase(currentRegionId, DateUtils.nowTimestampSec()) ?: run {
                        emit(WidgetInfo.NoCases)
                        return@flow
                    }
                debug("getWidgetInfo $regionInfo", TAG)

                val info = WidgetInfo.WidgetInfoData(
                    regionName = regionInfo.name,
                    regionDisease = "Covid-19",
                    regionCases = regionInfo.casesNumber.toInt().toString(),
                    regionSeverity = regionInfo.severity.toString(),
                    regionMaxSeverity = regionInfo.maxSeverity.let { "/$it" },
                    regionSeverityColor = regionInfo.color,
                    regionLegendDescription = regionInfo.severityInfo
                )
                debug("getWidgetInfo - info -$info", TAG)
                cacheWidgetInfoUseCase.setCached(info)
                emit(info)
                return@flow

            } catch (t: Throwable) {
                debug("getWidgetInfo ERROR - no internet", TAG, t)
                emit(cacheWidgetInfoUseCase.getCached() ?: WidgetInfo.Error)
                return@flow
            }

        } else {
            debug("getWidgetInfo ERROR - no location permission", TAG)
            emit(WidgetInfo.NoLocation)
            return@flow
        }
    }

    private fun Location.toPosition() = Position(lat = latitude, lon = longitude)

    private fun testingDummy() = WidgetInfo.WidgetInfoData(
        regionName = "Leipzig",
        regionDisease = "Covid-19",
        regionCases = "71",
        regionSeverity = "4",
        regionMaxSeverity = "/8",
        regionSeverityColor = "#A33438",
        regionLegendDescription = ""
    )
}

sealed class WidgetInfo {

    @Serializable
    data class WidgetInfoData(
        val regionName: String,
        val regionDisease: String,
        val regionCases: String,
        val regionSeverity: String,
        val regionMaxSeverity: String,
        val regionSeverityColor: String,
        val regionLegendDescription: String
    ) : WidgetInfo()

    object Loading: WidgetInfo()
    object NoLocation : WidgetInfo()
    object NoCases : WidgetInfo()
    object Error: WidgetInfo()
}