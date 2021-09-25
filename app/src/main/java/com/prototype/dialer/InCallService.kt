package com.prototype.dialer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.telecom.Call
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import android.telecom.InCallService as BaseService


class InCallService : BaseService() {

    private val notificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val channelId by lazy {
        getString(R.string.calling_channel_id)
    }

    /**
     * Обработка входящего звонка
     */
    override fun onCallAdded(call: Call) {

        val channelId =
            if (Build.VERSION.SDK_INT >= 26) {
                createNotificationChannel()
            } else {
                ""
            }

        // Создаем намерение, которое вызывает наш полноэкранный интерфейс входящего вызова.

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.flags = Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClass(applicationContext, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                this, 1, intent,
                if (Build.VERSION.SDK_INT >= 31)
                    PendingIntent.FLAG_MUTABLE
                else
                    PendingIntent.FLAG_ONE_SHOT
            )

        // Создаем уведомление с высоким приоритетом.
        // Это гарантирует, что оно отобразится в системной шапке и шторке.

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.in_call_title))
            .setContentText(call.details.callerDisplayName)
            .setSmallIcon(R.drawable.ic_logo)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)
            //.addAction(..)  // to add buttons to answer or reject the call.
            .build()

        notificationManager.notify(1, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val name = getString(R.string.app_name)
        val channel = NotificationChannel(
            channelId,
            name,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            setSound(
                ringtoneUri,
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
        }
        notificationManager.createNotificationChannel(channel)
        return channelId
    }
}
