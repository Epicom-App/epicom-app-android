package org.ebolapp.ui.map.wrapper

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.proxy.maps.Map as GoogleMap
import com.proxy.maps.MapFragment as SupportMapFragment
import com.proxy.maps.model.*
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.entities.MapRegionWithGeometry
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.ui.map.MapFragment
import org.ebolapp.ui.map.R
import org.ebolapp.utils.DateUtils

fun MapFragment.setupGoogleMaps(onDone: (map: GoogleMap) -> Unit) {
    (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.let { mapFragment ->
        mapFragment.getMapAsync { map ->
            getMapStyleOptions()?.let { map.setMapStyle(it) }
            onDone(map)
        }
    }
}

fun MapFragment.getMapStyleOptions() : MapStyleOptions? {
    val context = this.context ?: return null
    val uiModeFlags = context.resources?.configuration?.uiMode ?: return null
    return when (uiModeFlags and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> MapStyleOptions.loadRawResourceStyle(context, R.raw.ui_map_night_style)
        else -> MapStyleOptions.loadRawResourceStyle(context, R.raw.ui_map_default_style)
    }
}

internal fun MapWrapper.getMarker(
    context: Context,
    @DrawableRes drawableId: Int
): BitmapDescriptor? {
    val drawable = AppCompatResources.getDrawable(context, drawableId)!!
    val canvas = Canvas()
    val bitmap: Bitmap = Bitmap.createBitmap(
        drawable.minimumWidth,
        drawable.minimumHeight,
        Bitmap.Config.ARGB_8888
    )
    canvas.setBitmap(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

internal fun MapWrapper.getMarkerDescription(visit: Visit): String {
    return DateUtils.formattedPeriodBetween(visit.startTimestamp, visit.endTimestamp)
}

internal fun MapWrapper.getPolygonBounds(latLonList: List<LatLng>): LatLngBounds {
    return with(LatLngBounds.Builder()) {
        latLonList.forEach { include(it) }
        build()
    }
}

internal fun MapRegionWithGeometry.centerLatLon() : LatLng {
    val builder = LatLngBounds.Builder()
    this.geoRings.forEach { geoRing ->
        geoRing.forEach { position ->
            builder.include(position.toLatLon())
        }
    }
    return builder.build().center
}


internal fun Position.toLatLon(): LatLng = LatLng(this.lat, this.lon)

// Note: google maps uses topRight and bottomLeft
internal fun LatLngBounds.toMapRegionBox(): MapRegionBox =
    MapRegionBox(
        topLeft = Position(
            lat = northeast.latitude,
            lon = southwest.longitude
        ),
        bottomRight = Position(
            lat = southwest.latitude,
            lon = northeast.longitude
        )
    )
