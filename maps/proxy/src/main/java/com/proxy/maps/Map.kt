package com.proxy.maps

import com.proxy.maps.model.*

@Suppress("TooManyFunctions")
interface Map {

    val uiSettings: UiSettings
    val projection: Projection?
    val cameraPosition: CameraPosition

    var isMyLocationEnabled: Boolean

    fun addCircle(options: CircleOptions): Circle
    fun addMarker(options: MarkerOptions): Marker
    fun addPolygon(options: PolygonOptions): Polygon
    fun addPolyline(options: PolylineOptions): Polyline

    fun setOnCameraIdleListener(listener: OnCameraIdleListener)

    fun setOnMapClickListener(listener: OnMapClickListener)
    fun setOnMapClickListener(onMapClicked: (LatLng) -> Unit)

    fun setOnMarkerClickListener(listener: OnMarkerClickListener)
    fun setOnMarkerClickListener(onMarkerClicked: (Marker) -> Boolean)

    fun setOnCameraMoveStartedListener(listener: OnCameraMoveStartedListener)
    fun setOnCameraMoveStartedListener(onCameraMoveStarted: (OnCameraMoveStartedListener.Reason) -> Unit)
    fun setOnCameraIdleListener(onCameraIdle: () -> Unit)

    fun setOnPolygonClickListener(onPolygonClicked: (Polygon) -> Unit)

    fun moveCamera(cameraUpdate: CameraUpdate)
    fun animateCamera(cameraUpdate: CameraUpdate)
    fun animateCamera(cameraUpdate: CameraUpdate, callback: CancelableCallback)
    fun animateCamera(cameraUpdate: CameraUpdate, durationMs: Int, callback: CancelableCallback)

    fun setMapStyle(style: MapStyleOptions)

    fun clear()

    interface OnCameraIdleListener {
        fun onCameraIdle()
    }

    interface OnMapClickListener {
        fun onMapClick(latLng: LatLng)
    }

    interface OnMarkerClickListener {
        fun onMarkerClick(marker: Marker): Boolean
    }

    interface OnCameraMoveStartedListener {
        fun onCameraMoveStarted(reason: Reason)
        sealed class Reason {
            object ApiAnimation : Reason()
            object DeveloperAnimation : Reason()
            object Gesture : Reason()
        }
    }

    interface CancelableCallback {
        fun onCancel()
        fun onFinish()
    }
}
