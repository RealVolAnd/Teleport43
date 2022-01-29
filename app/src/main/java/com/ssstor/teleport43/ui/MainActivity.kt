package com.ssstor.teleport43.ui

import android.Manifest
import android.app.role.RoleManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
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
    private val REQUEST_CODE = 1010
    private var isHelpVisible = false

    var REQUIRED_PERMISSIONS_SDK_29_AND_ABOVE = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
       // setAppAsMockProvider()
        initViews()
        checkPermissions()
        setPowerMode()
        startMyService()
        setBroadcastReceiver()
    }

    fun setAppAsMockProvider() {
        startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
    }

    private fun checkPermissions(){
        requestPermissions(
            REQUIRED_PERMISSIONS_SDK_29_AND_ABOVE,
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here

                } else {
                    // permission is denied, you can ask for permission again, if you want
                    //  askForPermissions()

                }
                return
            }
        }
    }

    private fun  initViews(){

        vb.closeButton.setOnClickListener {
            stopMyService()
        }
        vb.mainHelpText.setOnClickListener {
            toggleHelpText()
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

    private fun toggleHelpText(){
        if(isHelpVisible){
            vb.mainHelpText.text = getString(R.string.help_short_text)
            isHelpVisible = false
        }else {
            vb.mainHelpText.text = "There are three things must be checked if App not works properly:" +
                    "\n" +
                    "\n1. GPS must be ON" +
                    "\n2. Teleport43 must be set as Mock location provider in Phone->Settings->For Developers" +
                    "\n3. Google Geolocation must be OFF"

            isHelpVisible = true
        }
    }

}