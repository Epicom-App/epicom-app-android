package com.proxy.maps.google

import androidx.annotation.RequiresPermission
import com.proxy.maps.CameraUpdate
import com.proxy.maps.Map
import com.proxy.maps.Projection
import com.proxy.maps.UiSettings
import com.proxy.maps.google.models.*
import com.proxy.maps.google.models.toCameraPosition
import com.proxy.maps.google.models.toCircle
import com.proxy.maps.google.models.toGoogleCircleOptions
import com.proxy.maps.google.models.toGoogleMarkerOptions
import com.proxy.maps.google.models.toLatLng
import com.proxy.maps.google.models.toMarker
import com.proxy.maps.google.models.toProjection
import com.proxy.maps.model.*
import com.google.android.gms.maps.GoogleMap as GMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener as GOnCameraMoveStartedListener

@Suppress("TooManyFunctions")
internal class GoogleMap(private val googleMap: GMap) : Map {

    override val uiSettings: UiSettings = object : UiSettings {

        override var isMapToolbarEnabled: Boolean
            get() = googleMap.uiSettings.isMapToolbarEnabled
            set(value) {
                googleMap.uiSettings.isMapToolbarEnabled = value
            }
        override var isZoomControlsEnabled: Boolean
            get() = googleMap.uiSettings.isZoomControlsEnabled
            set(value) {
                googleMap.uiSettings.isCompassEnabled = value
            }
        override var isIndoorLevelPickerEnabled: Boolean
            get() = googleMap.uiSettings.isIndoorLevelPickerEnabled
            set(value) {
                googleMap.uiSettings.isIndoorLevelPickerEnabled = value
            }
        override var isCompassEnabled: Boolean
            get() = googleMap.uiSettings.isCompassEnabled
            set(value) {
                googleMap.uiSettings.isCompassEnabled = value
            }
        override var isMyLocationButtonEnabled: Boolean
            get() = googleMap.uiSettings.isMyLocationButtonEnabled
            set(value) {
                googleMap.uiSettings.isMyLocationButtonEnabled = value
            }

        override fun setAllGesturesEnabled(enabled: Boolean) {
            googleMap.uiSettings.setAllGesturesEnabled(enabled)
        }
    }

    override val projection: Projection
        get() = googleMap.projection.toProjection()

    override val cameraPosition: CameraPosition
        get() = googleMap.cameraPosition.toCameraPosition()

    override var isMyLocationEnabled: Boolean
        @RequiresPermission(
            anyOf = [
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION"
            ]
        )
        get() = googleMap.isMyLocationEnabled
        @RequiresPermission(
            anyOf = [
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION"
            ]
        )
        set(value) {
            googleMap.isMyLocationEnabled = value
        }

    override fun addCircle(options: CircleOptions): Circle =
        googleMap.addCircle(options.toGoogleCircleOptions()).toCircle()

    override fun addMarker(options: MarkerOptions): Marker =
        googleMap.addMarker(options.toGoogleMarkerOptions()).toMarker()

    override fun addPolygon(options: PolygonOptions): Polygon {
        val googleOptions = options.toGooglePolygonOptions()
        val pl = googleMap.addPolygon(googleOptions)
        return pl.toPolygon()
    }

    override fun addPolyline(options: PolylineOptions): Polyline =
        googleMap.addPolyline(options.toGooglePolylineOptions()).toPolyline()

    override fun setOnCameraIdleListener(listener: Map.OnCameraIdleListener) =
        googleMap.setOnCameraIdleListener { listener.onCameraIdle() }

    override fun setOnCameraIdleListener(onCameraIdle: () -> Unit) {
        googleMap.setOnCameraIdleListener(onCameraIdle)
    }

    override fun setOnMapClickListener(listener: Map.OnMapClickListener) =
        googleMap.setOnMapClickListener { googleLatLng ->
            listener.onMapClick(googleLatLng.toLatLng())
        }

    override fun setOnMapClickListener(onMapClicked: (LatLng) -> Unit) =
        googleMap.setOnMapClickListener { googleLatLng ->
            onMapClicked(googleLatLng.toLatLng())
        }

    override fun setOnMarkerClickListener(listener: Map.OnMarkerClickListener) =
        googleMap.setOnMarkerClickListener {
            listener.onMarkerClick(it.toMarker())
        }

    override fun setOnMarkerClickListener(onMarkerClicked: (Marker) -> Boolean) =
        googleMap.setOnMarkerClickListener {
            onMarkerClicked(it.toMarker())
        }

    override fun setOnCameraMoveStartedListener(listener: Map.OnCameraMoveStartedListener) =
        googleMap.setOnCameraMoveStartedListener { reason ->
            listener.onCameraMoveStarted(toReason(reason))
        }

    override fun setOnCameraMoveStartedListener(
        onCameraMoveStarted: (Map.OnCameraMoveStartedListener.Reason) -> Unit
    ) = googleMap.setOnCameraMoveStartedListener { reason ->
        onCameraMoveStarted(toReason(reason))
    }

    override fun setOnPolygonClickListener(onPolygonClicked: (Polygon) -> Unit) {
        googleMap.setOnPolygonClickListener {
            onPolygonClicked(it.toPolygon())
        }
    }

    private fun toReason(reason: Int): Map.OnCameraMoveStartedListener.Reason =
        when (reason) {
            GOnCameraMoveStartedListener.REASON_API_ANIMATION ->
                Map.OnCameraMoveStartedListener.Reason.ApiAnimation
            GOnCameraMoveStartedListener.REASON_GESTURE ->
                Map.OnCameraMoveStartedListener.Reason.Gesture
            GOnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION ->
                Map.OnCameraMoveStartedListener.Reason.DeveloperAnimation
            else -> throw IllegalArgumentException(
                "Undefined CameraMoveStartedListener reason=$reason"
            )
        }

    override fun moveCamera(cameraUpdate: CameraUpdate) =
        googleMap.moveCamera((cameraUpdate as CameraUpdateWrapper).googleCameraUpdate)

    override fun animateCamera(cameraUpdate: CameraUpdate) =
        googleMap.animateCamera((cameraUpdate as CameraUpdateWrapper).googleCameraUpdate)

    override fun animateCamera(
        cameraUpdate: CameraUpdate,
        callback: Map.CancelableCallback
    ) {
        googleMap.animateCamera(
            (cameraUpdate as CameraUpdateWrapper).googleCameraUpdate,
            object : GMap.CancelableCallback {
                override fun onFinish() = callback.onFinish()
                override fun onCancel() = callback.onCancel()
            }
        )
    }

    override fun animateCamera(
        cameraUpdate: CameraUpdate,
        durationMs: Int,
        callback: Map.CancelableCallback
    ) {
        googleMap.animateCamera(
            (cameraUpdate as CameraUpdateWrapper).googleCameraUpdate,
            durationMs,
            object : GMap.CancelableCallback {
                override fun onFinish() = callback.onFinish()
                override fun onCancel() = callback.onCancel()
            }
        )
    }

    override fun setMapStyle(style: MapStyleOptions) {
        // TODO
    }

    override fun clear() = googleMap.clear()
}
