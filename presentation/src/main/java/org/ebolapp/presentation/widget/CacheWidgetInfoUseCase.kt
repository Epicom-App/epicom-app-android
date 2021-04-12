package org.ebolapp.presentation.widget

import android.content.Context
import androidx.core.content.edit
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface CacheWidgetInfoUseCase {
    fun getCached() : WidgetInfo.WidgetInfoData?
    fun setCached(data: WidgetInfo.WidgetInfoData)
}

class CacheWidgetInfoUseCaseImpl(
    private val context: Context
): CacheWidgetInfoUseCase {

    companion object {
        private const val PREFS_NAME = "org.ebolapp.widget.prefs"
        private const val PREFS_PROPERTY_CACHE = "org.ebolapp.widget.cache"
    }

    override fun getCached(): WidgetInfo.WidgetInfoData? {
        return try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.getString(PREFS_PROPERTY_CACHE, null)?.let { Json.decodeFromString(it) }
        } catch (e: Throwable) {
            null
        }
    }

    override fun setCached(data: WidgetInfo.WidgetInfoData) {
        try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
                val json = Json.encodeToString(data)
                putString(PREFS_PROPERTY_CACHE, json)
                apply()
            }
        } catch (e: Throwable) {
            return
        }
    }
}