package de.ebolapp.app

import android.app.Application
import de.ebolapp.app.di.appModules
import de.ebolapp.app.usecases.RunOnAppStartUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin

@KoinApiExtension
@ExperimentalCoroutinesApi
@FlowPreview
class App : Application() {

    private val runOnAppStartUseCase by inject<RunOnAppStartUseCase>()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModules)
        }
        runOnAppStartUseCase()
    }
}
