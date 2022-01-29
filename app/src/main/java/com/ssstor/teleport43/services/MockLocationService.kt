package com.ssstor.teleport43.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Process
import com.ssstor.teleport43.R
import com.ssstor.teleport43.ui.MainActivity

class MockLocationService: Service() {
    private val CHANNEL_ID = "COMMON_CHANNEL"
    private val NOTIFICATION_MAIN_ID = 100
    private lateinit var notificationManager: NotificationManager
    private val channelDefault = NotificationChannel(
        CHANNEL_ID, "Common channel",
        NotificationManager.IMPORTANCE_LOW
    )
    private lateinit var notification: Notification
    lateinit var resultPendingIntent: PendingIntent


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        channelDefault.setSound(null, null)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channelDefault)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        val openActivityIntent = Intent(this, MainActivity::class.java)
        resultPendingIntent = PendingIntent.getActivity(
            this,
            0, openActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Teleport43")
            .setContentText("Status: Active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(resultPendingIntent)
            .setTicker("ticker")
            .build()

        startForeground(NOTIFICATION_MAIN_ID, notification)



        Thread {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            checkButtons()
        }.start()
        return START_STICKY
    }
}