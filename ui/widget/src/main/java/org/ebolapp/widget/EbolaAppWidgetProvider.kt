package org.ebolapp.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.widget.RemoteViews
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter
import org.ebolapp.presentation.widget.WidgetInfo
import org.ebolapp.presentation.widget.WidgetViewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext


@KoinApiExtension
class EbolaAppWidgetProvider : AppWidgetProvider(), KoinComponent, Logging, CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    private val viewModel by inject<WidgetViewModel>()

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

        debug("UpdatingWidget START", Filter.BACKGROUND_JOB_LOG)

        context ?: return
        appWidgetManager ?: return
        appWidgetIds ?: return

        launch {
            viewModel.getWidgetInfo().collectLatest { widgetInfo ->
                updateWidgets(context, appWidgetManager, appWidgetIds, widgetInfo)
                debug("UpdatingWidget DONE", Filter.BACKGROUND_JOB_LOG)
                super.onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        debug("WidgetOptionsChanged START", Filter.BACKGROUND_JOB_LOG)

        context ?: return
        appWidgetManager ?: return
        val appWidgetIds = IntArray(1) { appWidgetId }

        launch {
            viewModel.getWidgetInfo().collectLatest { widgetInfo ->
                updateWidgets(context, appWidgetManager, appWidgetIds , widgetInfo)
                debug("UpdatingWidget DONE", Filter.BACKGROUND_JOB_LOG)
                super.onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    private fun updateWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
        widgetInfo: WidgetInfo
    ) {
        val remoteViews = RemoteViews(context.packageName, R.layout.ebola_app_widget)
        appWidgetIds.forEach { widgetId ->
            remoteViews.setOnClickListener(context, appWidgetManager, widgetId)
            remoteViews.updateInfo(context, widgetInfo)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        debug("UpdatingWidget: onDeleted")
        coroutineContext.cancel()
        super.onDeleted(context, appWidgetIds)
    }

    override fun onDisabled(context: Context?) {
        debug("UpdatingWidget: onDisabled")
        coroutineContext.cancel()
        super.onDisabled(context)
    }
}