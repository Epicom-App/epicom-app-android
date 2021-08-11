package com.proxy.maps.huawei

import androidx.annotation.RequiresPermission
import com.proxy.maps.CameraUpdate
import com.proxy.maps.Map
import com.proxy.maps.Projection
import com.proxy.maps.UiSettings
import com.proxy.maps.huawei.models.*
import com.proxy.maps.huawei.models.toHuaweiCircleOptions
import com.proxy.maps.huawei.models.toHuaweiMarkerOptions
import com.proxy.maps.huawei.models.toLatLng
import com.proxy.maps.huawei.models.toMarker
import com.proxy.maps.model.*
import com.huawei.hms.maps.HuaweiMap as HMap
import com.huawei.hms.maps.HuaweiMap.OnCameraMoveStartedListener as HOnCameraMoveStartedListener

@Suppress("TooManyFunctions")
internal class HuaweiMap(private val huaweiMap: HMap) : Map {

    override val uiSettings: UiSettings = object : UiSettings {

        override var isMapToolbarEnabled: Boolean
            get() = huaweiMap.uiSettings.isMapToolbarEnabled
            set(value) {
                huaweiMap.uiSettings.isMapToolbarEnabled = value
            }

        override var isZoomControlsEnabled: Boolean
            get() = huaweiMap.uiSettings.isZoomControlsEnabled
            set(value) {
                huaweiMap.uiSettings.isCompassEnabled = value
            }

        override var isIndoorLevelPickerEnabled: Boolean
            get() = huaweiMap.uiSettings.isIndoorLevelPickerEnabled
            set(value) {
                huaweiMap.uiSettings.isIndoorLevelPickerEnabled = value
            }

        override var isCompassEnabled: Boolean
            get() = huaweiMap.uiSettings.isCompassEnabled
            set(value) {
                huaweiMap.uiSettings.isCompassEnabled = value
            }

        override var isMyLocationButtonEnabled: Boolean
            get() = huaweiMap.uiSettings.isMyLocationButtonEnabled
            set(value) {
                huaweiMap.uiSettings.isMyLocationButtonEnabled = value
            }

        override fun setAllGesturesEnabled(enabled: Boolean) =
            huaweiMap.uiSettings.setAllGesturesEnabled(enabled)
    }

    override val projection: Projection
        get() = huaweiMap.projection.toProjection()

    override val cameraPosition: CameraPosition
        get() = huaweiMap.cameraPosition.toCameraPosition()

    override var isMyLocationEnabled: Boolean
        @RequiresPermission(
            anyOf = [
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION"
            ]
        )
        get() = huaweiMap.isMyLocationEnabled
        @RequiresPermission(
            anyOf = [
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.ACCESS_FINE_LOCATION"
            ]
        )
        set(value) {
            huaweiMap.isMyLocationEnabled = value
        }

    override fun addCircle(options: CircleOptions): Circle =
        huaweiMap.addCircle(options.toHuaweiCircleOptions()).toCircle()

    override fun addMarker(options: MarkerOptions): Marker =
        huaweiMap.addMarker(options.toHuaweiMarkerOptions()).toMarker()

    override fun addPolygon(options: PolygonOptions): Polygon =
        huaweiMap.addPolygon(options.toHuaweiPolygonOptions()).toPolygon()

    override fun addPolyline(options: PolylineOptions): Polyline =
        huaweiMap.addPolyline(options.toHuaweiPolylineOptions()).toPolyline()

    override fun setOnCameraIdleListener(listener: Map.OnCameraIdleListener) =
        huaweiMap.setOnCameraIdleListener { listener.onCameraIdle() }

    override fun setOnCameraIdleListener(onCameraIdle: () -> Unit) =
        huaweiMap.setOnCameraIdleListener(onCameraIdle)

    override fun setOnMapClickListener(listener: Map.OnMapClickListener) =
        huaweiMap.setOnMapClickListener { huaweiLatLng ->
            listener.onMapClick(huaweiLatLng.toLatLng())
        }

    override fun setOnMapClickListener(onMapClicked: (LatLng) -> Unit) =
        huaweiMap.setOnMapClickListener { huaweiLatLng ->
            onMapClicked(huaweiLatLng.toLatLng())
        }

    override fun setOnMarkerClickListener(listener: Map.OnMarkerClickListener) =
        huaweiMap.setOnMarkerClickListener {
            listener.onMarkerClick(it.toMarker())
        }

    override fun setOnMarkerClickListener(onMarkerClicked: (Marker) -> Boolean) =
        huaweiMap.setOnMarkerClickListener {
            onMarkerClicked(it.toMarker())
        }

    override fun setOnCameraMoveStartedListener(listener: Map.OnCameraMoveStartedListener) =
        huaweiMap.setOnCameraMoveStartedListener { reason ->
            listener.onCameraMoveStarted(toReason(reason))
        }

    override fun setOnCameraMoveStartedListener(
        onCameraMoveStarted: (Map.OnCameraMoveStartedListener.Reason) -> Unit
    ) = huaweiMap.setOnCameraMoveStartedListener { reason ->
        onCameraMoveStarted(toReason(reason))
    }

    override fun setOnPolygonClickListener(onPolygonClicked: (Polygon) -> Unit) {
        huaweiMap.setOnPolygonClickListener {
            onPolygonClicked(it.toPolygon())
        }
    }

    private fun toReason(reason: Int): Map.OnCameraMoveStartedListener.Reason =
        when (reason) {
            HOnCameraMoveStartedListener.REASON_API_ANIMATION ->
                Map.OnCameraMoveStartedListener.Reason.ApiAnimation
            HOnCameraMoveStartedListener.REASON_GESTURE ->
                Map.OnCameraMoveStartedListener.Reason.Gesture
            HOnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION ->
                Map.OnCameraMoveStartedListener.Reason.DeveloperAnimation
            else -> throw IllegalArgumentException(
                "Undefined CameraMoveStartedListener reason=$reason"
            )
        }

    override fun moveCamera(cameraUpdate: CameraUpdate) =
        huaweiMap.moveCamera((cameraUpdate as CameraUpdateWrapper).huaweiCameraUpdate)

    override fun animateCamera(cameraUpdate: CameraUpdate) =
        huaweiMap.animateCamera((cameraUpdate as CameraUpdateWrapper).huaweiCameraUpdate)

    override fun animateCamera(
        cameraUpdate: CameraUpdate,
        callback: Map.CancelableCallback
    ) {
        huaweiMap.animateCamera(
            (cameraUpdate as CameraUpdateWrapper).huaweiCameraUpdate,
            object : HMap.CancelableCallback {
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
        huaweiMap.animateCamera(
            (cameraUpdate as CameraUpdateWrapper).huaweiCameraUpdate,
            durationMs,
            object : HMap.CancelableCallback {
                override fun onFinish() = callback.onFinish()
                override fun onCancel() = callback.onCancel()
            }
        )
    }

    override fun setMapStyle(style: MapStyleOptions) {
        //TODO("Not yet implemented")
    }

    override fun clear() = huaweiMap.clear()
}
