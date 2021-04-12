package org.ebolapp.features.background.updates.notificator

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eboalapp.ui.main.MainActivity
import org.ebolapp.features.background.updates.R
import org.ebolapp.features.riskmatching.entities.NotNotifiedRiskMatch
import org.ebolapp.features.riskmatching.usecase.MarkAsNotifiedRiskMatchesUseCase
import org.ebolapp.features.riskmatching.usecase.RiskMatchType


interface Notificator {
    fun notifyNewRiskMatches(newRiskMatches: List<NotNotifiedRiskMatch>)
    fun setupRiskMatchesNotificationChannel()
}

internal const val NOTIFICATION_ID = 1234

internal class NotificatorImpl(
    private val context: Context,
    private val markAsNotifiedRiskMatchesUseCase: MarkAsNotifiedRiskMatchesUseCase
) : Notificator {

    override fun notifyNewRiskMatches(newRiskMatches: List<NotNotifiedRiskMatch>) {

        setupRiskMatchesNotificationChannel()

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val riskMatchType = newRiskMatches.lastOrNull()?.type ?: return
        val infoResource = riskMatchType.let {
            if (it == RiskMatchType.SEVERITY_INCREASE) R.string.new_risk_match_notification_severity_increased_info
            else R.string.new_risk_match_notification_region_changed_info
        }

        val notificationBuilder = NotificationCompat.Builder(context, RISK_MATCHES_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_small_icon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setContentTitle(context.resources.getString(R.string.new_risk_match_notification_title))
            .setContentText(context.resources.getString(infoResource))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }

        GlobalScope.launch {
            newRiskMatches
                .map { it.riskMatch }
                .also { markAsNotifiedRiskMatchesUseCase(it) }
        }
    }

    override fun setupRiskMatchesNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.risk_match_notification_channel_name)
            val descriptionText =
                context.getString(R.string.risk_match_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(RISK_MATCHES_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val RISK_MATCHES_CHANNEL_ID = "RiskMatchesChannelId"
    }
}
