package de.ebolapp.app.di

import kotlinx.coroutines.FlowPreview
import org.ebolapp.features.background.updates.backgroundUpdatesModule
import org.ebolapp.features.geotracking.geoTrackingModule
import org.ebolapp.features.permissions.permissionsModule
import org.ebolapp.presentation.presentationModule

@FlowPreview
val appModules = listOf(
    environmentModule,
    useCasesModule,
    presentationModule,
    backgroundUpdatesModule,
    geoTrackingModule,
    permissionsModule,
)
