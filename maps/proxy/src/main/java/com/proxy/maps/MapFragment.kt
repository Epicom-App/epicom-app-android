package com.proxy.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.ServiceLoader

interface DelegateMapFragment {
    @IdRes
    fun fragmentId(): Int
    @LayoutRes
    fun fragmentLayoutId(): Int
    fun getMapAsync(fragment: Fragment, callback: OnMapReadyCallback)
}

class MapFragment : Fragment() {

    private val delegateMapFragment =
        ServiceLoader.load(DelegateMapFragment::class.java).single()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(delegateMapFragment.fragmentLayoutId(), container, false)

    @UiThread
    fun getMapAsync(callback: OnMapReadyCallback) {
        if (view != null) {
            getMapAsyncInternal(callback)
        } else {
            parentFragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewCreated(
                        fragmentManager: FragmentManager,
                        fragment: Fragment,
                        view: View,
                        savedInstanceState: Bundle?
                    ) {
                        fragmentManager.unregisterFragmentLifecycleCallbacks(this)
                        getMapAsyncInternal(callback)
                    }
                },
                false
            )
        }
    }

    @UiThread
    fun getMapAsync(block: (Map) -> Unit) {
        val callback = object : OnMapReadyCallback {
            override fun onMapReady(map: Map?) {
                map?.let(block)
            }
        }
        if (view != null) {
            getMapAsyncInternal(callback)
        } else {
            parentFragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewCreated(
                        fragmentManager: FragmentManager,
                        fragment: Fragment,
                        view: View,
                        savedInstanceState: Bundle?
                    ) {
                        fragmentManager.unregisterFragmentLifecycleCallbacks(this)
                        getMapAsyncInternal(callback)
                    }
                },
                false
            )
        }
    }

    private fun getMapAsyncInternal(callback: OnMapReadyCallback) {
        val fragment = childFragmentManager
            .findFragmentById(delegateMapFragment.fragmentId())
            ?: throw NullPointerException()
        delegateMapFragment.getMapAsync(fragment = fragment, callback = callback)
    }

    companion object {
        fun newInstance() = MapFragment()
    }
}
