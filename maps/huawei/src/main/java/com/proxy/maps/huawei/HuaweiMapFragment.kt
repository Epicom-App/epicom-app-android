package com.proxy.maps.huawei

import androidx.fragment.app.Fragment
import com.huawei.hms.maps.SupportMapFragment
import com.proxy.maps.DelegateMapFragment
import com.proxy.maps.OnMapReadyCallback

internal class HuaweiMapFragment : DelegateMapFragment {

    private val packageName: String =
        this::class.java.`package`?.name ?: throw InternalError()

    override fun fragmentId(): Int = Class
        .forName("$packageName.R\$id")
        .getField("internal_map_fragment")
        .getInt(null)

    override fun fragmentLayoutId(): Int = Class
        .forName("$packageName.R\$layout")
        .getField("huawei_map_view")
        .getInt(null)

    override fun getMapAsync(
        fragment: Fragment,
        callback: OnMapReadyCallback
    ) = (fragment as SupportMapFragment)
        .getMapAsync { huaweiMap ->
            callback.onMapReady(HuaweiMap(huaweiMap))
        }
}
