package com.proxy.maps.model

@Suppress("LongParameterList", "MagicNumber")
class CircleOptions(
    val center: LatLng = LatLng(latitude = 51.226538, longitude = 6.762998),
    val fillColor: Int = 0,
    val radius: Double = .0,
    val strokeColor: Int = -16_777_216,
    val strokePattern: List<PatternItem>? = null,
    val strokeWidth: Float = 10f,
    val zIndex: Float = 0f,
    val clickable: Boolean = false,
    val visible: Boolean = true
) {

    fun center(center: LatLng) = CircleOptions(
        center = center,
        fillColor = this.fillColor,
        radius = this.radius,
        strokeColor = this.strokeColor,
        strokePattern = this.strokePattern,
        strokeWidth = this.strokeWidth,
        zIndex = this.zIndex,
        clickable = this.clickable,
        visible = this.visible
    )

    fun fillColor(color: Int) = CircleOptions(
        center = this.center,
        fillColor = color,
        radius = this.radius,
        strokeColor = this.strokeColor,
        strokePattern = this.strokePattern,
        strokeWidth = this.strokeWidth,
        zIndex = this.zIndex,
        clickable = this.clickable,
        visible = this.visible
    )

    fun radius(radius: Double) = CircleOptions(
        center = this.center,
        fillColor = this.fillColor,
        radius = radius,
        strokeColor = this.strokeColor,
        strokePattern = this.strokePattern,
        strokeWidth = this.strokeWidth,
        zIndex = this.zIndex,
        clickable = this.clickable,
        visible = this.visible
    )

    fun strokeColor(color: Int) = CircleOptions(
        center = this.center,
        fillColor = this.fillColor,
        radius = this.radius,
        strokeColor = color,
        strokePattern = this.strokePattern,
        strokeWidth = this.strokeWidth,
        zIndex = this.zIndex,
        clickable = this.clickable,
        visible = this.visible
    )

    fun strokePattern(strokePattern: List<PatternItem>) = CircleOptions(
        center = this.center,
        fillColor = this.fillColor,
        radius = this.radius,
        strokeColor = this.strokeColor,
        strokePattern = strokePattern,
        strokeWidth = this.strokeWidth,
        zIndex = this.zIndex,
        clickable = this.clickable,
        visible = this.visible
    )

    fun strokeWidth(width: Float) = CircleOptions(
        center = this.center,
        fillColor = this.fillColor,
        radius = this.radius,
        strokeColor = this.strokeColor,
        strokePattern = strokePattern,
        strokeWidth = width,
        zIndex = this.zIndex,
        clickable = this.clickable,
        visible = this.visible
    )

    fun zIndex(zIndex: Float) = CircleOptions(
        center = this.center,
        fillColor = this.fillColor,
        radius = this.radius,
        strokeColor = this.strokeColor,
        strokePattern = strokePattern,
        strokeWidth = this.strokeWidth,
        zIndex = zIndex,
        clickable = this.clickable,
        visible = this.visible
    )

    fun clickable(clickable: Boolean) = CircleOptions(
        center = this.center,
        fillColor = this.fillColor,
        radius = this.radius,
        strokeColor = this.strokeColor,
        strokePattern = strokePattern,
        strokeWidth = this.strokeWidth,
        zIndex = this.zIndex,
        clickable = clickable,
        visible = this.visible
    )

    fun visible(visible: Boolean) = CircleOptions(
        center = this.center,
        fillColor = this.fillColor,
        radius = this.radius,
        strokeColor = this.strokeColor,
        strokePattern = strokePattern,
        strokeWidth = this.strokeWidth,
        zIndex = this.zIndex,
        clickable = clickable,
        visible = visible
    )
}
