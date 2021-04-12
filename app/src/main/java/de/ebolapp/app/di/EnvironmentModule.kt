package de.ebolapp.app.di

import android.content.Context
import de.ebolapp.BuildConfig
import de.ebolapp.R
import org.ebolapp.features.cases.network.Endpoints
import org.ebolapp.features.permissions.controller.ResourceConfiguration
import org.ebolapp.features.regions.utils.JsonFileReader
import org.ebolapp.db.DatabaseDriverFactory
import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.features.staticPages.network.StaticPages
import org.ebolapp.shared.network.NetworkApiParameterNames
import org.ebolapp.shared.network.NoParametersStrategy
import org.ebolapp.shared.network.TemplateParametersStrategy
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.dsl.module

val environmentModule = module {
    // Networking Api
    single {
        Endpoints(
            mapStatesCases = TemplateParametersStrategy(
                endpoint = BuildConfig.BaseURL + "/DEU/{date}/regions.json",
                templates = mapOf(NetworkApiParameterNames.TIMESTAMP to "{date}")
            ),
            mapRegionCases = TemplateParametersStrategy(
                endpoint = BuildConfig.BaseURL + "/DEU/{date}/areas.json",
                templates = mapOf(NetworkApiParameterNames.TIMESTAMP to "{date}")
            ),
            mapRegionCasesLegend = TemplateParametersStrategy(
                endpoint = BuildConfig.FilesURL + "/{language}/legend.json",
                templates = mapOf(NetworkApiParameterNames.LANGUAGE to "{language}")
            )
        )
    }
    // Static pages
    single {
        StaticPages(
            imprint = TemplateParametersStrategy(
                endpoint = BuildConfig.FilesURL + "/{language}/imprint.html",
                templates = mapOf(NetworkApiParameterNames.LANGUAGE to "{language}")
            ),
            dataPrivacy = TemplateParametersStrategy(
                endpoint = BuildConfig.FilesURL + "/{language}/dataPrivacy.html",
                templates = mapOf(NetworkApiParameterNames.LANGUAGE to "{language}")
            ),
            about = TemplateParametersStrategy(
                endpoint = BuildConfig.FilesURL + "/{language}/about.html",
                templates = mapOf(NetworkApiParameterNames.LANGUAGE to "{language}")
            )
        )
    }
    // Database Driver
    single {
        DatabaseWrapper(
            DatabaseDriverFactory(context = get())
        )
    }
    single(AppCenterSecretQualifier) { "e98b58b4-cdec-4085-b5fa-de8299812d3f" }
    // Resources
    single {
        // TODO add a general resource provider so that submodules can access app wide resources
        ResourceConfiguration(
            rationalTitleResId = R.string.location_rationale_title,
            rationalMessageResId = R.string.location_rationale_message,
            settingsTitleResId = R.string.no_location_title,
            settingsMessageResId = R.string.no_location_info,
            okButton = R.string.ok_button,
            openSettingsButton = R.string.open_settings_button
        )
    }
    factory<JsonFileReader>(StatesJsonQualifier) {
        val context: Context = get()
        object : JsonFileReader {
            override suspend fun get(): String {
                return context.assets.open("json/states.json").bufferedReader()
                    .use { it.readText() }
            }
        }
    }
    factory<JsonFileReader>(StatesDistrictsJsonQualifier) {
        val context: Context = get()
        object : JsonFileReader {
            override suspend fun get(): String {
                return context.assets.open("json/districts.json").bufferedReader()
                    .use { it.readText() }
            }
        }
    }
}

object StatesJsonQualifier : Qualifier {
    override val value: QualifierValue = "StatesJson"
}

object StatesDistrictsJsonQualifier : Qualifier {
    override val value: QualifierValue = "StatesDistrictsJson"
}

object AppCenterSecretQualifier: Qualifier {
    override val value: QualifierValue = "AppCenterSecret"
}

