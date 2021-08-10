package com.proxy.maps

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import java.util.ServiceLoader

interface DelegateMapViewClassName {
    fun get(): String
}

@Suppress("TooManyFunctions")
interface DelegateMapView {
    fun onCreateDelegate(bundle: Bundle?)
    fun onResumeDelegate()
    fun onPauseDelegate()
    fun onStartDelegate()
    fun onStopDelegate()
    fun onDestroyDelegate()
    fun onLowMemoryDelegate()
    fun onSaveInstanceStateDelegate(bundle: Bundle?)
    fun getMapAsyncDelegate(callback: OnMapReadyCallback)
    fun onEnterAmbientDelegate(bundle: Bundle?)
    fun onExitAmbientDelegate()
}

class MapView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private val delegateMapViewClassName =
        ServiceLoader.load(DelegateMapViewClassName::class.java).single()
    private val delegateMapView: DelegateMapView

    init {
        with(delegateMapView(context, attributeSet)) {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            this@MapView.addView(this)
            delegateMapView = this as DelegateMapView
        }
    }

    private fun delegateMapView(
        context: Context,
        attributeSet: AttributeSet
    ): View = MapView::class.java.classLoader
        ?.loadClass(delegateMapViewClassName.get())
        ?.getDeclaredConstructor(Context::class.java, AttributeSet::class.java)
        ?.newInstance(context, attributeSet) as View

    fun getMapAsync(callback: OnMapReadyCallback) {
        delegateMapView.getMapAsyncDelegate(callback)
    }

    fun onCreate(bundle: Bundle?) {
        delegateMapView.onCreateDelegate(bundle)
    }

    fun onSaveInstanceState(bundle: Bundle) {
        delegateMapView.onSaveInstanceStateDelegate(bundle)
    }

    fun onResume() {
        delegateMapView.onResumeDelegate()
    }

    fun onStart() {
        delegateMapView.onStartDelegate()
    }

    fun onPause() {
        delegateMapView.onPauseDelegate()
    }

    fun onStop() {
        delegateMapView.onStopDelegate()
    }

    fun onDestroy() {
        delegateMapView.onDestroyDelegate()
    }

    fun onLowMemory() {
        delegateMapView.onLowMemoryDelegate()
    }
}
