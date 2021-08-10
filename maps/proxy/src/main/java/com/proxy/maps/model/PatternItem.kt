package com.proxy.maps.model

sealed class PatternItem

object Dot : PatternItem()
class Dash(val length: Float) : PatternItem()
class Gap(val length: Float) : PatternItem()
