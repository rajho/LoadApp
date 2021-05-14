package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.util.cancelNotifications
import com.udacity.util.sendNotification
import com.udacity.view.ButtonState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var mChecked: Int = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private val urls = listOf(
        "https://github.com/bumptech/glide/archive/master.zip",
        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",
        "https://github.com/square/retrofit/archive/master.zip"
    )
    private val downloadMap = mutableMapOf<Long, String>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            custom_button.setLoadingButtonState(ButtonState.Completed)

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            id?.let {
                if (it != -1L){
                    val url = downloadMap[it]
                    val projectNumber = urls.indexOf(url) + 1

                    context?.let { ctx ->
                        val downloadStatus = checkDownloadStatus(ctx, it)
                        notificationManager.sendNotification(CHANNEL_ID, ctx, projectNumber, downloadStatus)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager = getSystemService(NotificationManager::class.java)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(CHANNEL_ID, CHANNEL_ID)

        options_group.setOnCheckedChangeListener { group, checkedId ->
            mChecked = checkedId
        }

        custom_button.setOnClickListener {
            if (mChecked == 0) {
                Toast.makeText(this, "Please select the file to download", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            notificationManager.cancelNotifications()
            when(mChecked) {
                radio_button_1.id -> download(urls.elementAt(0))
                radio_button_2.id -> download(urls.elementAt(1))
                radio_button_3.id -> download(urls.elementAt(2))
                else -> download(urls.elementAt(1))
            }
        }
    }

    private fun download(url: String) {
        custom_button.setLoadingButtonState(ButtonState.Loading)

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(getString(R.string.app_name))
            .setDescription(getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        // enqueue puts the download request in the queue.
        downloadID = downloadManager.enqueue(request)
        downloadMap[downloadID] = url
    }

    private fun checkDownloadStatus(context: Context, downloadId: Long) : String {
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)

        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

            return if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                "Success"
            } else {
                "Fail"
            }
        }

        return ""
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                    .apply {
                        enableLights(true)
                        lightColor = Color.RED
                        enableVibration(true)
                        description = "Downloads made"
                        setShowBadge(false)
                    }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }

}
