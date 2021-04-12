package org.ebolapp.load.regions.entities

import org.ebolapp.features.regions.entities.Position

data class DistrictRegion(
    val areaID: String,
    val bundeslandID: String,
    val areaName: String,
    val geoRing: List<List<Position>>
)