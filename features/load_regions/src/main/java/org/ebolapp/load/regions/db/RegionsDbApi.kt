package org.ebolapp.load.regions.db


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ebolapp.db.MapRegionGeoRingTable
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.load.regions.entities.DistrictRegion
import org.ebolapp.load.regions.entities.StateRegion
import org.ebolapp.db.DatabaseWrapper

interface RegionsDbApi {
    // Map regions

    suspend fun countParentMapRegions(): Long
    suspend fun countChildMapRegions(): Long
    suspend fun insertParentMapRegion(parentMapRegions: Map<StateRegion, MapRegionBox>)
    suspend fun insertChildMapRegion(childMapRegions: Map<DistrictRegion, MapRegionBox>)

    suspend fun deleteMapRegions()
}

internal class  RegionsDbApiImpl(
    databaseWrapper: DatabaseWrapper
): RegionsDbApi {

    private val database = databaseWrapper.database

    private val mapRegionQueries = database.mapRegionTableQueries
    private val mapRegionGeoRingQueries = database.mapRegionGeoRingTableQueries

    override suspend fun countParentMapRegions(): Long =
        withContext(Dispatchers.Default) {
            mapRegionQueries.countParents().executeAsOne()
        }

    override suspend fun countChildMapRegions(): Long =
        withContext(Dispatchers.Default) {
            mapRegionQueries.countChildren().executeAsOne()
        }

    override suspend fun insertParentMapRegion(
        parentMapRegions: Map<StateRegion, MapRegionBox>
    ) = withContext(Dispatchers.Default) {
        database.transaction {
            parentMapRegions.forEach { (state, stateMapBox) ->
                // MapRegionTable
                mapRegionQueries.insertParent(
                    id = state.id,
                    name = state.name,
                    topLeftLat = stateMapBox.topLeft.lat,
                    topLeftLon = stateMapBox.topLeft.lon,
                    bottomRightLat = stateMapBox.bottomRight.lat,
                    bottomRightLon = stateMapBox.bottomRight.lon
                )
                // MapRegionGeoRing
                state.geoRing.forEachIndexed { ringIndex, stateGeoRing ->
                    stateGeoRing.forEach { (lat, lon) ->
                        mapRegionGeoRingQueries.insertMapRegionGeoRing(
                            MapRegionGeoRingTable(
                                regionId = state.id,
                                ringIndex = ringIndex.toLong(),
                                lat = lat,
                                lon = lon
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun insertChildMapRegion(
        childMapRegions: Map<DistrictRegion, MapRegionBox>
    ) = withContext(Dispatchers.Default) {
        database.transaction {
            childMapRegions.forEach { (district, districtMapBox) ->
                // MapRegionTable
                mapRegionQueries.insertChild(
                    id = district.areaID,
                    name = district.areaName,
                    parentId = district.bundeslandID,
                    topLeftLat = districtMapBox.topLeft.lat,
                    topLeftLon = districtMapBox.topLeft.lon,
                    bottomRightLat = districtMapBox.bottomRight.lat,
                    bottomRightLon = districtMapBox.bottomRight.lon
                )
                // MapRegionGeoRing
                district.geoRing.forEachIndexed { ringIndex, districtGeoRing ->
                    districtGeoRing.forEach { (lat, lon) ->
                        mapRegionGeoRingQueries.insertMapRegionGeoRing(
                            MapRegionGeoRingTable(
                                regionId = district.areaID,
                                ringIndex = ringIndex.toLong(),
                                lat = lat,
                                lon = lon
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun deleteMapRegions() =
        withContext(Dispatchers.Default) {
            database.transaction {
                mapRegionQueries.deleteAll()
                mapRegionGeoRingQueries.deleteAll()
            }
        }
}