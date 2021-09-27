package com.prototype.dialer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.telecom.Call
import android.telecom.CallAudioState
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.prototype.dialer.core.extension.TAG
import android.telecom.InCallService as BaseService

/**
 * Сервис взаимодействия с подсистемой телефона
 */
class InCallService : BaseService() {

    private val notificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val channelId by lazy {
        getString(R.string.calling_channel_id)
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind")
        return super.onBind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState) {
        Log.d(TAG, "onCallAudioStateChanged: $audioState")
        super.onCallAudioStateChanged(audioState)
    }

    override fun onBringToForeground(showDialpad: Boolean) {
        Log.d(TAG, "onBringToForeground")
        super.onBringToForeground(showDialpad)
    }

    /**
     * Обработка входящего звонка
     */
    override fun onCallAdded(call: Call) {
        Log.d(TAG, "onCallAdded: $call")

        val channelId =
            if (Build.VERSION.SDK_INT >= 26) {
                createNotificationChannel()
            } else {
                ""
            }

        // Создаем намерение, для вызова окна с входящим звонком из шторки.

        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            flags = Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK
            setClass(applicationContext, MainActivity::class.java)
        }.apply {
            putExtra(MainActivity.STATE_KEY, MainActivity.State.INCOMING_CALL.name)
        }

        val flags = if (Build.VERSION.SDK_INT >= 31) FLAG_MUTABLE else FLAG_ONE_SHOT
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, flags)

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

        notificationManager.notify(ACTIVE_CALL_NOTIFICATION_ID, notification)

        // Показываем окно с входящим звонком

        startActivity(intent)
    }

    override fun onCallRemoved(call: Call) {
        Log.d(TAG, "onCallRemoved: $call")
        super.onCallRemoved(call)
    }

    override fun onCanAddCallChanged(canAddCall: Boolean) {
        Log.d(TAG, "onCanAddCallChanged")
        super.onCanAddCallChanged(canAddCall)
    }

    override fun onSilenceRinger() {
        Log.d(TAG, "onSilenceRinger")
        super.onSilenceRinger()
    }

    override fun onConnectionEvent(call: Call, event: String, extras: Bundle?) {
        Log.d(TAG, "onConnectionEvent")
        super.onConnectionEvent(call, event, extras)
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

    companion object {
        const val ACTIVE_CALL_NOTIFICATION_ID = 1
    }
}
