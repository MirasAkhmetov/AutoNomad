package com.autonomad

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.autonomad.ui.MainActivity
import com.autonomad.utils.timber
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationsService : FirebaseMessagingService() {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "com.autonomad"
        private const val NOTIFICATION_CHANNEL_NAME = "Notifications channel"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notification channel description"

        const val PUSH_NOTIFICATION_TYPE = "push_notification_type"
    }

    override fun onCreate() {
        createNotificationChannel()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        timber("messageId: ${remoteMessage.messageId}")
        timber("data: ${remoteMessage.data}")
        if (remoteMessage.data.isNotEmpty()) sendNotification(remoteMessage.data)
    }

    private fun sendNotification(data: Map<String, String>) {
        createNotificationChannel()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            for ((k, v) in data) {
                putExtra(k, v)
            }
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_transparent)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
            .setContentTitle(data["title"])
            .setContentText(data["body"])
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(0, notificationBuilder.build()) // todo id, channel name description
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = NOTIFICATION_CHANNEL_DESCRIPTION
                enableLights(true)
                vibrationPattern = arrayOf<Long>(0, 500, 500, 500).toLongArray()
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    override fun onNewToken(token: String) {
        timber("Refreshed token: $token")
    }
}