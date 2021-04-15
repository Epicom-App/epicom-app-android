package org.ebolapp.features.permissions.controller

import androidx.annotation.StringRes

data class ResourceConfiguration(
    @StringRes val rationalTitleResId: Int,
    @StringRes val rationalMessageResId: Int,
    @StringRes val settingsTitleResId: Int,
    @StringRes val settingsMessageResId: Int,
    @StringRes val consentTitleResId: Int,
    @StringRes val consentMessageResId: Int,
    @StringRes val okButton: Int,
    @StringRes val openSettingsButton: Int,
    @StringRes val acceptButton: Int,
    @StringRes val declineButton: Int,
)