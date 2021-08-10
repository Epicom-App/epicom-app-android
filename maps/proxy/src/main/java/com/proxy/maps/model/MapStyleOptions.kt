package com.proxy.maps.model

import android.content.Context
import android.content.res.Resources


class MapStyleOptions {


    companion object {

        @Throws(Resources.NotFoundException::class)
        fun loadRawResourceStyle(
            var0: Context,
            var1: Int
        ): MapStyleOptions = MapStyleOptions()
    }
}