package com.proxy.maps.google

import android.graphics.Point
import com.proxy.maps.CameraUpdate
import com.proxy.maps.DelegateCameraUpdateFactory
import com.proxy.maps.google.models.toGoogleCameraPosition
import com.proxy.maps.google.models.toGoogleLatLng
import com.proxy.maps.google.models.toGoogleLatLngBounds
import com.proxy.maps.model.CameraPosition
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.LatLngBounds
import com.google.android.gms.maps.CameraUpdateFactory as GoogleCameraUpdateFactory

internal class GoogleCameraUpdateFactory : DelegateCameraUpdateFactory {

    override fun newCameraPosition(cameraPosition: CameraPosition): CameraUpdate =
        CameraUpdateWrapper(GoogleCameraUpdateFactory.newCameraPosition(cameraPosition.toGoogleCameraPosition()))

    override fun newLatLng(latLng: LatLng): CameraUpdate =
        CameraUpdateWrapper(GoogleCameraUpdateFactory.newLatLng(latLng.toGoogleLatLng()))

    override fun newLatLngBounds(
        bounds: LatLngBounds,
        width: Int,
        height: Int,
        padding: Int
    ): CameraUpdate =
        CameraUpdateWrapper(
            GoogleCameraUpdateFactory.newLatLngBounds(
                bounds.toGoogleLatLngBounds(),
                width,
                height,
                padding
            )
        )

    override fun newLatLngBounds(
        bounds: LatLngBounds,
        padding: Int
    ): CameraUpdate =
        CameraUpdateWrapper(
            GoogleCameraUpdateFactory.newLatLngBounds(
                bounds.toGoogleLatLngBounds(),
                padding
            )
        )

    override fun newLatLngZoom(latLng: LatLng, zoom: Float): CameraUpdate =
        CameraUpdateWrapper(
            GoogleCameraUpdateFactory.newLatLngZoom(
                latLng.toGoogleLatLng(),
                zoom
            )
        )

    override fun scrollBy(xPixel: Float, yPixel: Float): CameraUpdate =
        CameraUpdateWrapper(GoogleCameraUpdateFactory.scrollBy(xPixel, yPixel))

    override fun zoomBy(amount: Float, focus: Point): CameraUpdate =
        CameraUpdateWrapper(GoogleCameraUpdateFactory.zoomBy(amount, focus))

    override fun zoomBy(amount: Float): CameraUpdate =
        CameraUpdateWrapper(GoogleCameraUpdateFactory.zoomBy(amount))

    override fun zoomIn(): CameraUpdate =
        CameraUpdateWrapper(GoogleCameraUpdateFactory.zoomIn())

    override fun zoomOut(): CameraUpdate =
        CameraUpdateWrapper(GoogleCameraUpdateFactory.zoomOut())

    override fun zoomTo(zoom: Float): CameraUpdate =
        CameraUpdateWrapper(GoogleCameraUpdateFactory.zoomTo(zoom))
}
