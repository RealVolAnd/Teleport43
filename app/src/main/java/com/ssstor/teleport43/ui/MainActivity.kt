package com.ssstor.teleport43.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ssstor.teleport43.*
import com.ssstor.teleport43.BROADCAST_CLOSE_APP
import com.ssstor.teleport43.databinding.ActivityMainBinding
import com.ssstor.teleport43.services.MockLocationService

class MainActivity : AppCompatActivity() {
    private lateinit var vb : ActivityMainBinding
    private lateinit var pm: PowerManager
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        initViews()
        setPowerMode()
        startMyService()

        setBroadcastReceiver()


    }

    private fun  initViews(){

        vb.closeButton.setOnClickListener {
            stopMyService()
        }
    }

    private fun setPowerMode() {
        val s = packageName
        pm = getSystemService(POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            val intentIgnoreBattery = Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.parse("package:$packageName")
            )
            startActivity(intentIgnoreBattery)
        }
    }

    private fun startMyService(){
        startService(Intent(this, MockLocationService::class.java))
    }

    private fun stopMyService(){
        stopService(Intent(this, MockLocationService::class.java))
    }

    private fun setBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                val intentExtraCmd = intent?.getIntExtra("cmd", 0)
                val intentExtraData = intent?.getStringExtra("data")
                if (intentExtraCmd!! > 0) {
                    when (intentExtraCmd) {
                        BROADCAST_CLOSE_APP -> {
                            finishAffinity()
                        }
                    }
                }
            }
        }
        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(broadcastReceiver, IntentFilter(ACTION))
    }

}