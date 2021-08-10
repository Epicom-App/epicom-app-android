package com.proxy.maps.huawei

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.annotation.Keep
import com.proxy.maps.DelegateMapView
import com.proxy.maps.DelegateMapViewClassName
import com.proxy.maps.OnMapReadyCallback
import com.huawei.hms.maps.MapView as HuaweiMapView

internal class HuaweiDelegateMapViewClassName : DelegateMapViewClassName {
    override fun get(): String = HuaweiDelegateMapView::class.java.name
}

@Keep
internal class HuaweiDelegateMapView(
    context: Context,
    attributeSet: AttributeSet
) : HuaweiMapView(context, attributeSet), DelegateMapView {

    override fun onCreateDelegate(bundle: Bundle?) = onCreate(bundle)

    override fun onStartDelegate() = onStart()

    override fun onStopDelegate() = onStop()

    override fun onResumeDelegate() = onResume()

    override fun onPauseDelegate() = onPause()

    override fun onDestroyDelegate() = onDestroy()

    override fun onLowMemoryDelegate() = onLowMemory()

    override fun onSaveInstanceStateDelegate(bundle: Bundle?) = onSaveInstanceState(bundle)

    override fun getMapAsyncDelegate(callback: OnMapReadyCallback) =
        getMapAsync { callback.onMapReady(HuaweiMap(it)) }

    override fun onEnterAmbientDelegate(bundle: Bundle?) = onEnterAmbient(bundle)

    override fun onExitAmbientDelegate() = onExitAmbient()
}
