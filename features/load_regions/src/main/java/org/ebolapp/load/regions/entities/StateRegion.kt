package org.ebolapp.load.regions.entities

import org.ebolapp.features.regions.entities.Position

data class StateRegion(
    val id: String,
    val name: String,
    val geoRing: List<List<Position>>
)