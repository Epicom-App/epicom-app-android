package org.ebolapp.load.regions.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface ImportStateDataSource {
    fun get(): RegionsImportState
    fun set(state:RegionsImportState)
    fun observe(): Flow<RegionsImportState>
}

class ImportStateDataSourceImpl(
    private val sharedPrefs: SharedPreferences
): ImportStateDataSource {

    override fun get(): RegionsImportState {
        return when(val state = sharedPrefs.getString(KEY, STATE_INITIAL)) {
            STATE_INITIAL -> RegionsImportState.Initial
            STATE_STARTED -> RegionsImportState.Started
            STATE_COMPLETE -> RegionsImportState.Complete
            else -> RegionsImportState.Initial
        }
    }

    override fun set(state: RegionsImportState) {
        sharedPrefs.edit(commit = true) {
            this.putString(KEY,
                when (state) {
                    is RegionsImportState.Initial -> STATE_INITIAL
                    is RegionsImportState.Started -> STATE_STARTED
                    is RegionsImportState.Complete -> STATE_COMPLETE
                }
            )
        }
    }

    override fun observe(): Flow<RegionsImportState> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY) {
                offer(get())
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    companion object {
        const val KEY = "ImportState"
        const val STATE_INITIAL = "Initial"
        const val STATE_STARTED = "Started"
        const val STATE_COMPLETE = "Complete"
    }
}

sealed class RegionsImportState {
    object Initial: RegionsImportState()
    object Started: RegionsImportState()
    object Complete: RegionsImportState()
}