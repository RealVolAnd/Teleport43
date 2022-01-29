package com.ssstor.teleport43.services

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.Process
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ssstor.teleport43.*
import com.ssstor.teleport43.ui.MainActivity
import java.io.IOException
import android.location.LocationManager
import android.location.provider.ProviderProperties

import android.os.AsyncTask
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.ssstor.teleport43.entities.LocationItem
import java.lang.Exception
import java.lang.NullPointerException


class MockLocationService: Service(){
    private val CHANNEL_ID = "COMMON_CHANNEL"
    private val NOTIFICATION_MAIN_ID = 100
    private lateinit var notificationManager: NotificationManager
    private val channelDefault = NotificationChannel(
        CHANNEL_ID, "Common channel",
        NotificationManager.IMPORTANCE_LOW
    )
    private lateinit var notification: Notification
    lateinit var resultPendingIntent: PendingIntent
    private var currentBlankMediaPlayersList = arrayListOf<MediaPlayer>()

   val mock: MockLocationProvider = MockLocationProvider(LocationManager.GPS_PROVIDER)

    private val trackData:ArrayList<String> = arrayListOf()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        channelDefault.setSound(null, null)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channelDefault)
    }

    override fun onDestroy() {
        stopBlankMediaPlayers()
        super.onDestroy()
        stopApplication()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val openActivityIntent = Intent(this, MainActivity::class.java)

        trackData.add("15.13137,29.99708,16")

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
        startPlayBlankAudio()
        startMock()
        return START_STICKY
    }


    private fun startMock(){
        Thread{
            while(true){
                mock.pushLocation(-12.34, 23.45)
                Thread.sleep(1000)
            }
        }.start()
    }

    private fun startPlayBlankAudio() {
        val mediaPlayer = MediaPlayer()
        currentBlankMediaPlayersList.add(mediaPlayer)
        try {
            mediaPlayer.setDataSource(App.instance.baseContext, Uri.parse(AUDIO_BLANK_PATH))
            mediaPlayer.isLooping = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            mediaPlayer.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Thread {
            mediaPlayer.start()
        }.start()
        Log.d("@@@","Blank started")
    }

    private fun stopBlankMediaPlayers() {
        if (currentBlankMediaPlayersList.isNotEmpty()) {
            currentBlankMediaPlayersList.forEach { it.release() }
            currentBlankMediaPlayersList.clear()
            Log.d("@@@","Blank stopped")
        }
    }

    private fun stopApplication() {
        sendCmdToPresenter(BROADCAST_CLOSE_APP, "0")
    }

    private fun sendCmdToPresenter(cmd: Int, data: String) {
        val broadcastIntent = Intent(ACTION)
        broadcastIntent.putExtra("cmd", cmd)
        broadcastIntent.putExtra("data", data)
        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        LocalBroadcastManager.getInstance(App.instance.applicationContext)
            .sendBroadcast(broadcastIntent)
    }
}