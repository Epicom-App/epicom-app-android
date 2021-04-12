package org.ebolapp.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.ebolapp.logging.Logging
import org.ebolapp.logging.debug
import org.ebolapp.logging.tags.Filter
import org.ebolapp.presentation.widget.WidgetViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext


class EbolaAppWidgetUpdateService : Service(), KoinComponent, Logging, CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    private val viewModel by inject<WidgetViewModel>()

    override fun onBind(intent: Intent?): IBinder? {
        debug("On Bind", Filter.BACKGROUND_JOB_LOG)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        debug("onStartCommand", Filter.BACKGROUND_JOB_LOG)

        val context = this.baseContext
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val widgetProvider = ComponentName(this, EbolaAppWidgetProvider::class.java)


        launch {
            viewModel.getWidgetInfo().collectLatest { widgetInfo ->
                debug("onCollectInfo = $widgetInfo", Filter.BACKGROUND_JOB_LOG)
                val remoteViews = RemoteViews(packageName, R.layout.ebola_app_widget)
                remoteViews.setOnClickListener(context, appWidgetManager)
                remoteViews.updateInfo(context, widgetInfo)
                appWidgetManager.updateAppWidget(widgetProvider, remoteViews)
                debug("onUpdateWidgetInServiceDone", Filter.BACKGROUND_JOB_LOG)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}