package org.ebolapp.ui.map.wrapper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import androidx.core.graphics.ColorUtils
import com.proxy.maps.CameraUpdateFactory
import com.proxy.maps.Map as GoogleMap
import com.proxy.maps.Map.OnCameraMoveStartedListener.Reason.Gesture
import com.proxy.maps.model.*
import kotlinx.coroutines.*
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.entities.MapRegionWithGeometry
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.ui.map.R
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@OptIn(
    KoinApiExtension::class,
    ExperimentalCoroutinesApi::class,
    FlowPreview::class
)
class MapWrapper(
    private val uiScope: CoroutineScope,
    private val context: Context,
    private val map: GoogleMap,
    private val onRegionSelected: (mapRegion: MapRegionWithGeometry) -> Unit,
    private val onDoneDrawingMapRegions: () -> Unit,
    private val onMapMovedByUser: () -> Unit,
    private val onMapMoved: (mapRegionBox: MapRegionBox) -> Unit
) : KoinComponent {

    private val defaultLocation = LatLng(51.304647, 10.405560)

    private val displayedRegionsPolygonsMap = mutableMapOf<MapRegionWithGeometry, List<Polygon>>()
    private val selectedRegionPolyline: MutableList<Polyline> = mutableListOf()

    private val userLocationForDayMarkersList = mutableListOf<Marker>()
    private val riskyUserLocationForDayMarkersList = mutableListOf<Marker>()

    private var initialZoomDone = false

    var mapPadding = ZOOM_PADDING_DEFAULT
        set(value) {
            field = (value * ZOOM_PADDING_PERCENT).toInt()
        }

    init {
        map.setOnPolygonClickListener { clickedPolygon ->
            findRiskAreaByPolygon(clickedPolygon)?.let { clickedMapRegion ->
                selectRegionInternal(clickedMapRegion)
                onRegionSelected(clickedMapRegion)
            }
        }
        map.setOnCameraMoveStartedListener { reason ->
            when (reason) {
                Gesture -> {
                    deselectRegionInternal()
                    onMapMovedByUser()
                }
                else -> {}
            }
        }
        map.setOnCameraIdleListener {
            onMapMoved(mapRegionBox())
        }
    }

    private fun findRiskAreaByPolygon(polygon: Polygon): MapRegionWithGeometry? {
        return displayedRegionsPolygonsMap.keys.find { it.id == polygon.tag }
    }

    @SuppressLint("MissingPermission")
    fun setHasLocationPermission(hasLocationPermission: Boolean) {
        map.isMyLocationEnabled = hasLocationPermission
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isMapToolbarEnabled = false
    }

    fun moveToDefaultLocation() {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                defaultLocation,
                INITIAL_ZOOM_LEVEL
            )
        )
    }

    fun moveToCurrentLocation(location: Location) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude
            ),
            CURRENT_LOCATION_ZOOM_LEVEL
        ))
    }

    private fun moveToRegionLocation(mapRegion: MapRegionWithGeometry) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
            mapRegion.centerLatLon(),
            if (mapRegion.parentId == null) SELECT_STATE_ZOOM_LEVEL
            else SELECT_REGION_ZOOM_LEVEL
        ))
    }

    fun moveToShowAllPositions(positions: List<Position>) {
        if (positions.count() == 0) return
        val builder = LatLngBounds.Builder()
        positions.forEach { builder.include(LatLng(it.lat, it.lon)) }
        val bounds = builder.build()
        if (areBoundsTooSmall(bounds, 500)) {
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(bounds.center, SELECT_LOCATIONS_ZOOM_LEVEL)
            map.animateCamera(cameraUpdate)
        } else {
            val paddingPx = MAP_PADDING_DP * context.resources.displayMetrics.density
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, paddingPx.toInt())
            map.animateCamera(cameraUpdate)
        }
    }

    private fun areBoundsTooSmall(bounds: LatLngBounds, minDistanceInMeters: Int): Boolean {
        val result = FloatArray(1)
        Location.distanceBetween(
            bounds.southwest.latitude,
            bounds.southwest.longitude,
            bounds.northeast.latitude,
            bounds.northeast.longitude,
            result
        )
        return result[0] < minDistanceInMeters
    }

    fun mapRegionBox(): MapRegionBox {
        return map.projection?.visibleRegion?.latLngBounds?.toMapRegionBox()!!
    }

    fun addMapRegions(regions: List<MapRegionWithGeometry>) {

        if (regions.isEmpty()) {
            onDoneDrawingMapRegions()
            return
        }

        val removedRegions = displayedRegionsPolygonsMap.keys - regions
        val newRegions = regions - displayedRegionsPolygonsMap.keys
        // remove
        removedRegions.forEach { removedRegion ->
            displayedRegionsPolygonsMap[removedRegion]?.forEach { polygon -> polygon.remove() }
            displayedRegionsPolygonsMap.remove(removedRegion)
        }
        // draw new
        newRegions.forEach { region ->
            val areaPolygons = mutableListOf<Polygon>()
            val color = ColorUtils.setAlphaComponent(Color.parseColor(region.color), 160)
            region.geoRings.forEach { geoRing ->
                val polygon = map.addPolygon(
                    PolygonOptions()
                        .clickable(true)
                        .strokeColor(context.resources.getColor(R.color.mapStrokeColor))
                        .strokeWidth(2.5f)
                        .fillColor(color)
                        .addAll(geoRing.map { it.toLatLon() })
                ).also { it.tag = region.id }
                areaPolygons.add(polygon)
            }
            displayedRegionsPolygonsMap[region] = areaPolygons
        }

        onDoneDrawingMapRegions()
    }

    private fun selectRegionInternal(mapRegion: MapRegionWithGeometry) {
        deselectRegionInternal()
        mapRegion.geoRings.forEach { geoRing ->
            val polyline = map.addPolyline(
                PolylineOptions()
                    .clickable(false)
                    .color(context.resources.getColor(R.color.mapStrokeColor))
                    .width(SELECTION_BORDER_WIDTH)
                    .zIndex(SELECTION_BORDER_Z_INDEX)
                    .addAll(geoRing.map { it.toLatLon() })
            )
            selectedRegionPolyline.add(polyline)
        }
        moveToRegionLocation(mapRegion)
    }

    fun deselectRegionInternal() {
        with(selectedRegionPolyline) {
            forEach { polyline -> polyline.remove() }
            clear()
        }
    }

    fun addUserVisitsForDayMarkers(userLocationsForDay: List<Visit>) {
        clearUserLocationsForDayMarkers()
        userLocationsForDay.forEach { visit ->
            val markerOptions = MarkerOptions()
                .position(visit.position.toLatLon())
                .icon(getMarker(context, R.drawable.ui_map_visit_marker))
                .title(context.getString(R.string.map_location_callout_title))
                .snippet(getMarkerDescription(visit))
            userLocationForDayMarkersList.add(map.addMarker(markerOptions))
        }
    }

    private fun clearUserLocationsForDayMarkers() {
        with(userLocationForDayMarkersList) {
            forEach { marker -> marker.remove() }
            clear()
        }
    }

    fun addRiskyVisitsForDayMarkers(riskyUserLocationsForDay: List<Visit>) {
        clearRiskyUserLocationsForDayMarkers()
        riskyUserLocationsForDay.forEach { riskyVisit ->
            val markerOptions = MarkerOptions()
                .position(riskyVisit.position.toLatLon())
                .icon(getMarker(context, R.drawable.ui_map_risk_match_marker))
                .title(context.getString(R.string.map_location_callout_title))
                .snippet(getMarkerDescription(riskyVisit))
            riskyUserLocationForDayMarkersList.add(map.addMarker(markerOptions))
        }
    }

    private fun clearRiskyUserLocationsForDayMarkers() {
        with(riskyUserLocationForDayMarkersList) {
            forEach { marker -> marker.remove() }
            clear()
        }
    }

    companion object {
        private const val SELECTION_BORDER_Z_INDEX = 20f
        private const val SELECTION_BORDER_WIDTH = 10f
        private const val ZOOM_PADDING_DEFAULT = 100
        private const val ZOOM_PADDING_PERCENT = 0.3f
        private const val INITIAL_ZOOM_LEVEL = 5.8f
        private const val CURRENT_LOCATION_ZOOM_LEVEL = 10f
        private const val SELECT_LOCATIONS_ZOOM_LEVEL = 15f
        private const val SELECT_REGION_ZOOM_LEVEL = 9f
        private const val SELECT_STATE_ZOOM_LEVEL = 7f
        private const val MAP_PADDING_DP = 32
    }
}
