package org.ebolapp.load.regions

import android.content.Context
import android.view.Display
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.ebolapp.load.regions.db.RegionsDbApi
import org.ebolapp.load.regions.db.RegionsDbApiImpl
import org.ebolapp.load.regions.parsers.RegionsParserImpl
import org.ebolapp.load.regions.preferences.ImportStateDataSource
import org.ebolapp.load.regions.preferences.ImportStateDataSourceImpl
import org.ebolapp.load.regions.usecases.ImportMapRegionsUseCase
import org.ebolapp.load.regions.usecases.ImportMapRegionsUseCaseImpl
import org.ebolapp.load.regions.usecases.ImportStateCheckUseCase
import org.ebolapp.load.regions.usecases.ImportStateCheckUseCaseImpl
import org.ebolapp.db.DatabaseWrapper

class LoadRegionsUseCaseFactory(
    context: Context,
    databaseWrapper: DatabaseWrapper
) {

    private val regionsDbApi: RegionsDbApi = RegionsDbApiImpl(databaseWrapper)

    private val moshi : Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val regionsParser : RegionsParserImpl = RegionsParserImpl(
        moshi,
        context.assets.open("json/states.json"),
        context.assets.open("json/districts.json")
    )

    private val importStatePreferences : ImportStateDataSource = ImportStateDataSourceImpl(
        context.getSharedPreferences("ImportState", Context.MODE_PRIVATE)
    )

    fun createImportMapRegionsUseCase() : ImportMapRegionsUseCase =
        ImportMapRegionsUseCaseImpl(
            regionsDbApi = regionsDbApi,
            regionsParser = regionsParser,
            importStateDataSource = importStatePreferences
        )

    fun createImportCheckStateUseCase() : ImportStateCheckUseCase =
        ImportStateCheckUseCaseImpl(
            importStatePreferences
        )
}