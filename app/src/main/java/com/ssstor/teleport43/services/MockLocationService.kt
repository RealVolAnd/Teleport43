package com.ssstor.teleport43.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ssstor.teleport43.*
import com.ssstor.teleport43.ui.MainActivity
import java.io.IOException


class MockLocationService : Service() {
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

    val mock_gps: MockLocationProvider = MockLocationProvider(LocationManager.GPS_PROVIDER)
    //val mock_net: MockLocationProvider = MockLocationProvider(LocationManager.NETWORK_PROVIDER)

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
        mock_gps.shutdown()
       // mock_net.shutdown()
        super.onDestroy()
        stopApplication()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val openActivityIntent = Intent(this, MainActivity::class.java)

        resultPendingIntent = PendingIntent.getActivity(
            this,
            0, openActivityIntent,  PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(App.CURRENT_ITEM.locationItemName)
            .setContentText("Click to open App interface")
            .setSubText("Active")
            .setSmallIcon(R.drawable.ic_stat_t43_notify_icon)
            .setContentIntent(resultPendingIntent)
            .setColorized(true)
            .setColor(getResources().getColor(R.color.main_title_color))
            .build()


        startForeground(NOTIFICATION_MAIN_ID, notification)
        startPlayBlankAudio()
        startMock()
        return START_STICKY
    }

    private fun updateNotification(){
        notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(App.CURRENT_ITEM.locationItemName)
            .setContentText("click to open App interface")
            .setSubText("Active")
            .setSmallIcon(R.drawable.ic_stat_t43_notify_icon)
            .setContentIntent(resultPendingIntent)
            .setColorized(true)
            .setColor(getResources().getColor(R.color.main_title_color))
            .build()
        notificationManager.notify(NOTIFICATION_MAIN_ID, notification)
    }


    private fun startMock() {
        Thread {
            var coordinates = arrayListOf<String>()
            var latArray = arrayListOf<Double>()
            var lonArray = arrayListOf<Double>()

            while (true) {
                if (App.locationChanged){
                    updateNotification()
                    coordinates.clear()
                    latArray.clear()
                    lonArray.clear()

                    if(App.CURRENT_ITEM.locationItemTrack.contains(";")){
                        coordinates = App.CURRENT_ITEM.locationItemTrack.split(";") as ArrayList<String>
                        coordinates.forEach {
                            if(it.contains(",")){
                                val tmpLatLon = it.split(",")
                                latArray.add(tmpLatLon[0].toDouble())
                                lonArray.add(tmpLatLon[1].toDouble())
                            }
                        }
                    } else {

                        if(App.CURRENT_ITEM.locationItemTrack.contains(",")){
                            val tmpLatLon = App.CURRENT_ITEM.locationItemTrack.split(",")
                            latArray.add(tmpLatLon[0].toDouble())
                            lonArray.add(tmpLatLon[1].toDouble())
                        }
                    }
                    App.locationChanged = false
                }

                for(i in latArray.indices){
                    if (App.locationChanged) break
                    mock_gps.pushLocation(latArray[i], lonArray[i], App.ALT)
                    Thread.sleep(COORDINATES_REFRESH_INTERVAL_IN_MS)

                    //mock_net.pushLocation(latArray[i], lonArray[i], App.ALT)
                   // Thread.sleep(COORDINATES_REFRESH_INTERVAL_IN_MS)
                }

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
        Log.d("@@@", "Blank started")
    }

    private fun stopBlankMediaPlayers() {
        if (currentBlankMediaPlayersList.isNotEmpty()) {
            currentBlankMediaPlayersList.forEach { it.release() }
            currentBlankMediaPlayersList.clear()
            Log.d("@@@", "Blank stopped")
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