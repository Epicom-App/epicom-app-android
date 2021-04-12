package org.ebolapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.RemoteViews
import org.eboalapp.ui.main.MainActivity
import org.ebolapp.presentation.widget.WidgetInfo
import org.ebolapp.ui.common.extensions.matchColor


internal fun RemoteViews.updateInfo(
    context: Context,
    info: WidgetInfo
) {
    when (info) {
        is WidgetInfo.WidgetInfoData -> updateOnSuccess(info)
        is WidgetInfo.NoCases,
        WidgetInfo.NoLocation,
        WidgetInfo.Error,
        WidgetInfo.Loading -> updateOnError(context.getErrorText(info))
    }
}

internal fun RemoteViews.updateOnSuccess(
    info: WidgetInfo.WidgetInfoData
) {

    setViewVisibility(R.id.region_overview, View.VISIBLE)
    setViewVisibility(R.id.region_name, View.VISIBLE)
    setViewVisibility(R.id.region_disease, View.VISIBLE)
    setViewVisibility(R.id.errorMessage, View.GONE)

    setTextViewText(R.id.region_name, info.regionName)
    setTextViewText(R.id.region_disease, info.regionDisease)
    setTextViewText(R.id.region_cases, info.regionCases)
    setTextViewText(R.id.region_severity, info.regionSeverity)
    setTextViewText(R.id.region_max_severity, info.regionMaxSeverity)
    setTextViewText(R.id.region_legend_description, info.regionLegendDescription)

    val severityBgColor = Color.parseColor(info.regionSeverityColor)
    val severityTextColor = severityBgColor.matchColor(onLightColor = Color.BLACK, onDarkColor = Color.WHITE)
    setInt(R.id.region_severity, "setTextColor", severityTextColor)
    setInt(R.id.region_max_severity, "setTextColor", severityTextColor)
    setInt(R.id.region_severity_label, "setTextColor", severityTextColor)
    setInt(
        R.id.background,
        "setColorFilter",
        Color.parseColor(info.regionSeverityColor)
    )
}

internal fun RemoteViews.updateOnError(
    errorMessage: String
) {
    setViewVisibility(R.id.region_overview, View.GONE)
    setViewVisibility(R.id.region_name, View.GONE)
    setViewVisibility(R.id.region_disease, View.GONE)
    setViewVisibility(R.id.errorMessage, View.GONE)
    setViewVisibility(R.id.errorMessage, View.VISIBLE)

    setTextViewText(R.id.errorMessage, errorMessage)
}


internal fun RemoteViews.setOnClickListener(
    context: Context,
    appWidgetManager: AppWidgetManager,
    widgetId: Int = 0
) {
    val configIntent = Intent(context, MainActivity::class.java)
    val configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0)

    setOnClickPendingIntent(R.id.contentRoot, configPendingIntent)
    appWidgetManager.updateAppWidget(widgetId, this)
}

internal fun Context?.getErrorText(
    info: WidgetInfo
): String {
    this?.resources ?: return "Something went wrong"
    return when (info) {
        is WidgetInfo.NoLocation ->
            resources.getString(R.string.widget_info_no_location_data)
        is WidgetInfo.NoCases ->
            resources.getString(R.string.widget_info_no_cases_data)
        is WidgetInfo.Loading ->
            resources.getString(R.string.loading_text)
        else -> resources.getString(R.string.widget_error_general)
    }
}