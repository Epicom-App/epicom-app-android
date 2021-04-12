package org.ebolapp.load.regions.usecases

import kotlinx.coroutines.flow.Flow
import org.ebolapp.load.regions.preferences.ImportStateDataSource
import org.ebolapp.load.regions.preferences.RegionsImportState

interface ImportStateCheckUseCase {
    fun check(): RegionsImportState
    fun observe(): Flow<RegionsImportState>
}

internal class ImportStateCheckUseCaseImpl(
    private val importStateDataSource: ImportStateDataSource
): ImportStateCheckUseCase {
    override fun check(): RegionsImportState =  importStateDataSource.get()
    override fun observe(): Flow<RegionsImportState>  = importStateDataSource.observe()
}