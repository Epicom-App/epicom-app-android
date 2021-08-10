package com.proxy.maps.model

@Suppress("LongParameterList")
class MarkerOptions(
    val alpha: Float = 1f,
    val anchorU: Float = .5f,
    val anchorV: Float = 1f,
    val flat: Boolean = false,
    val icon: BitmapDescriptor? = null,
    val infoWindowAnchorU: Float = .5f,
    val infoWindowAnchorV: Float = 0f,
    val position: LatLng? = null,
    val rotation: Float = 0f,
    val snippet: String? = null,
    val title: String? = null,
    val zIndex: Float = 0f,
    val draggable: Boolean = false,
    val visible: Boolean = true
) {

    fun position(latLng: LatLng?) = MarkerOptions(
        alpha = this.alpha,
        anchorU = this.anchorU,
        anchorV = this.anchorV,
        flat = this.flat,
        icon = this.icon,
        infoWindowAnchorU = this.infoWindowAnchorU,
        infoWindowAnchorV = this.infoWindowAnchorV,
        position = latLng,
        rotation = this.rotation,
        snippet = this.snippet,
        title = this.title,
        zIndex = this.zIndex,
        draggable = this.draggable,
        visible = this.visible
    )

    fun icon(bitmapDescriptor: BitmapDescriptor?) = MarkerOptions(
        alpha = this.alpha,
        anchorU = this.anchorU,
        anchorV = this.anchorV,
        flat = this.flat,
        icon = bitmapDescriptor,
        infoWindowAnchorU = this.infoWindowAnchorU,
        infoWindowAnchorV = this.infoWindowAnchorV,
        position = this.position,
        rotation = this.rotation,
        snippet = this.snippet,
        title = this.title,
        zIndex = this.zIndex,
        draggable = this.draggable,
        visible = this.visible
    )

    fun title(title: String) = MarkerOptions(
        alpha = this.alpha,
        anchorU = this.anchorU,
        anchorV = this.anchorV,
        flat = this.flat,
        icon = this.icon,
        infoWindowAnchorU = this.infoWindowAnchorU,
        infoWindowAnchorV = this.infoWindowAnchorV,
        position = this.position,
        rotation = this.rotation,
        snippet = this.snippet,
        title = title,
        zIndex = this.zIndex,
        draggable = this.draggable,
        visible = this.visible
    )

    fun snippet(snippet: String) = MarkerOptions(
        alpha = this.alpha,
        anchorU = this.anchorU,
        anchorV = this.anchorV,
        flat = this.flat,
        icon = this.icon,
        infoWindowAnchorU = this.infoWindowAnchorU,
        infoWindowAnchorV = this.infoWindowAnchorV,
        position = this.position,
        rotation = this.rotation,
        snippet = snippet,
        title = this.title,
        zIndex = this.zIndex,
        draggable = this.draggable,
        visible = this.visible
    )
}
