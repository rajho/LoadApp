package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

private const val DOWNLOAD_NOTIFICATION_ID = 1

fun NotificationManager.sendNotification(channelId: String, applicationContext: Context, projectNumber: Int, downloadStatus: String) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(Intent.EXTRA_INDEX, projectNumber)
    contentIntent.putExtra(Intent.EXTRA_TEXT, downloadStatus)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        DOWNLOAD_NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(applicationContext.getString(R.string.notification_description, projectNumber))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(DOWNLOAD_NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}