package com.proxy.maps.huawei

import android.graphics.Point
import com.huawei.hms.maps.CameraUpdateFactory as HuaweiCameraUpdateFactory
import com.proxy.maps.CameraUpdate
import com.proxy.maps.DelegateCameraUpdateFactory
import com.proxy.maps.huawei.models.toHuaweiCameraPosition
import com.proxy.maps.huawei.models.toHuaweiLatLng
import com.proxy.maps.huawei.models.toHuaweiLatLngBounds
import com.proxy.maps.model.CameraPosition
import com.proxy.maps.model.LatLng
import com.proxy.maps.model.LatLngBounds

internal class HuaweiCameraUpdateFactory : DelegateCameraUpdateFactory {

    override fun newCameraPosition(cameraPosition: CameraPosition): CameraUpdate =
        CameraUpdateWrapper(HuaweiCameraUpdateFactory.newCameraPosition(cameraPosition.toHuaweiCameraPosition()))

    override fun newLatLng(latLng: LatLng): CameraUpdate =
        CameraUpdateWrapper(HuaweiCameraUpdateFactory.newLatLng(latLng.toHuaweiLatLng()))

    override fun newLatLngBounds(
        bounds: LatLngBounds,
        width: Int,
        height: Int,
        padding: Int
    ): CameraUpdate =
        CameraUpdateWrapper(
            HuaweiCameraUpdateFactory.newLatLngBounds(
                bounds.toHuaweiLatLngBounds(),
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
            HuaweiCameraUpdateFactory.newLatLngBounds(
                bounds.toHuaweiLatLngBounds(),
                padding
            )
        )

    override fun newLatLngZoom(latLng: LatLng, zoom: Float): CameraUpdate =
        CameraUpdateWrapper(
            HuaweiCameraUpdateFactory.newLatLngZoom(
                latLng.toHuaweiLatLng(),
                zoom
            )
        )

    override fun scrollBy(xPixel: Float, yPixel: Float): CameraUpdate =
        CameraUpdateWrapper(HuaweiCameraUpdateFactory.scrollBy(xPixel, yPixel))

    override fun zoomBy(amount: Float, focus: Point): CameraUpdate =
        CameraUpdateWrapper(HuaweiCameraUpdateFactory.zoomBy(amount, focus))

    override fun zoomBy(amount: Float): CameraUpdate =
        CameraUpdateWrapper(HuaweiCameraUpdateFactory.zoomBy(amount))

    override fun zoomIn(): CameraUpdate =
        CameraUpdateWrapper(HuaweiCameraUpdateFactory.zoomIn())

    override fun zoomOut(): CameraUpdate =
        CameraUpdateWrapper(HuaweiCameraUpdateFactory.zoomOut())

    override fun zoomTo(zoom: Float): CameraUpdate =
        CameraUpdateWrapper(HuaweiCameraUpdateFactory.zoomTo(zoom))
}
