package com.proxy.maps

import android.graphics.Point
import com.proxy.maps.model.CameraPosition
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.LatLngBounds
import java.util.ServiceLoader

@Suppress("TooManyFunctions")
interface DelegateCameraUpdateFactory {
    fun newCameraPosition(cameraPosition: CameraPosition): CameraUpdate
    fun newLatLng(latLng: LatLng): CameraUpdate
    fun newLatLngBounds(bounds: LatLngBounds, width: Int, height: Int, padding: Int): CameraUpdate
    fun newLatLngBounds(bounds: LatLngBounds, padding: Int): CameraUpdate
    fun newLatLngZoom(latLng: LatLng, zoom: Float): CameraUpdate
    fun scrollBy(xPixel: Float, yPixel: Float): CameraUpdate
    fun zoomBy(amount: Float, focus: Point): CameraUpdate
    fun zoomBy(amount: Float): CameraUpdate
    fun zoomIn(): CameraUpdate
    fun zoomOut(): CameraUpdate
    fun zoomTo(zoom: Float): CameraUpdate
}

object CameraUpdateFactory :
    DelegateCameraUpdateFactory by
    ServiceLoader.load(DelegateCameraUpdateFactory::class.java).single()
