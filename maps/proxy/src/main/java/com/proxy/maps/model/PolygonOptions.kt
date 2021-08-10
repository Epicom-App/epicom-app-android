package com.proxy.maps.model

class PolygonOptions(
    var strokeWidth: Float = 10.0f,
    var strokeColor: Int = -16777216,
    var fillColor: Int = 0,
    var zIndex: Float = 0.0f,
    var isVisible: Boolean = true,
    var isGeodesic: Boolean = false,
    var isClickable: Boolean = false,
    var strokeJointType: Int = 0,
    val points: List<LatLng> = emptyList(),
    val holes: List<List<LatLng>> = emptyList(),
    val strokePattern: List<PatternItem> = emptyList(),
) {

    fun strokeWidth(strokeWidth: Float) = PolygonOptions(
        strokeWidth = strokeWidth,
        strokeColor = strokeColor,
        fillColor = this.fillColor,
        zIndex = this.zIndex,
        isVisible = this.isVisible,
        isGeodesic = this.isGeodesic,
        isClickable = this.isClickable,
        strokeJointType = this.strokeJointType,
        points = this.points,
        holes = this.holes,
        strokePattern = this.strokePattern
    )

    fun strokeColor(strokeColor: Int) = PolygonOptions(
        strokeWidth = this.strokeWidth,
        strokeColor = strokeColor,
        fillColor = this.fillColor,
        zIndex = this.zIndex,
        isVisible = this.isVisible,
        isGeodesic = this.isGeodesic,
        isClickable = this.isClickable,
        strokeJointType = this.strokeJointType,
        points = this.points,
        holes = this.holes,
        strokePattern = this.strokePattern
    )

    fun fillColor(fillColor: Int) = PolygonOptions(
        strokeWidth = this.strokeWidth,
        strokeColor = this.strokeColor,
        fillColor = fillColor,
        zIndex = this.zIndex,
        isVisible = this.isVisible,
        isGeodesic = this.isGeodesic,
        isClickable = this.isClickable,
        strokeJointType = this.strokeJointType,
        points = this.points,
        holes = this.holes,
        strokePattern = this.strokePattern
    )

    fun clickable(clickable: Boolean) = PolygonOptions(
        strokeWidth = this.strokeWidth,
        strokeColor = this.strokeColor,
        fillColor = this.fillColor,
        zIndex = this.zIndex,
        isVisible = this.isVisible,
        isGeodesic = this.isGeodesic,
        isClickable = clickable,
        strokeJointType = this.strokeJointType,
        points = this.points,
        holes = this.holes,
        strokePattern = this.strokePattern
    )

    fun addAll(points: List<LatLng>) = PolygonOptions(
        strokeWidth = this.strokeWidth,
        strokeColor = this.strokeColor,
        fillColor = this.fillColor,
        zIndex = this.zIndex,
        isVisible = this.isVisible,
        isGeodesic = this.isGeodesic,
        isClickable = this.isClickable,
        strokeJointType = this.strokeJointType,
        points = points,
        holes = this.holes,
        strokePattern = this.strokePattern
    )
}
