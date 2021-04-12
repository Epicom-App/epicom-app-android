package org.ebolapp.features.settings

import android.content.Context

private const val KEY_WAS_FINISHED = "was_finished"

class OnboardingSettingDataSource(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)

    fun setWasFinished(value: Boolean) =
        sharedPreferences.edit().putBoolean(KEY_WAS_FINISHED, value).apply()

    fun getWasFinished(): Boolean = sharedPreferences.getBoolean(KEY_WAS_FINISHED, false)
}
