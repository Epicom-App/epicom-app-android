package org.ebolapp.load.regions.usecases

import org.ebolapp.load.regions.db.RegionsDbApi
import org.ebolapp.load.regions.parsers.RegionsParser
import org.ebolapp.load.regions.preferences.ImportStateDataSource
import org.ebolapp.load.regions.preferences.RegionsImportState
import org.ebolapp.utils.MapUtils

interface ImportMapRegionsUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke()
}

internal class ImportMapRegionsUseCaseImpl(
    private val regionsDbApi: RegionsDbApi,
    private val regionsParser: RegionsParser,
    private val importStateDataSource: ImportStateDataSource
): ImportMapRegionsUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke() {
        when (importStateDataSource.get()) {
            RegionsImportState.Initial -> import()
            RegionsImportState.Started -> import()
            RegionsImportState.Complete -> { /* By Pass */ }
        }
    }

    private suspend fun import() {
        importStateDataSource.set(RegionsImportState.Started)
        regionsDbApi.deleteMapRegions()
        parseAndSaveStatesRegions()
        parseAndSaveDistrictRegions()
        importStateDataSource.set(RegionsImportState.Complete)
    }

    private suspend fun parseAndSaveStatesRegions() {
        val iterator = regionsParser.parseStates().iterator()
        while (iterator.hasNext()) {
            val state = iterator.next()
            val item = mapOf(state to MapUtils.calculateMapRegionBox(state.geoRing))
            regionsDbApi.insertParentMapRegion(item)
        }
    }

    private suspend fun parseAndSaveDistrictRegions() {
        val iterator = regionsParser.parseDistricts().iterator()
        while(iterator.hasNext()) {
            val district = iterator.next()
            val item = mapOf(district to MapUtils.calculateMapRegionBox(district.geoRing))
            regionsDbApi.insertChildMapRegion(item)
        }
    }
}


