package com.ssstor.teleport43.ui

import android.Manifest
import android.app.role.RoleManager
import android.content.*
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssstor.teleport43.*
import com.ssstor.teleport43.BROADCAST_CLOSE_APP
import com.ssstor.teleport43.databinding.ActivityMainBinding
import com.ssstor.teleport43.services.MockLocationService

class MainActivity : AppCompatActivity(), MainContract.View, BackButtonListener, OnItemClick {
    private lateinit var vb : ActivityMainBinding
    private lateinit var pm: PowerManager
    private lateinit var broadcastReceiver: BroadcastReceiver
    private val REQUEST_CODE = 1010
    private var isHelpVisible = false
    private var helpCheckListDone = 0
    private val sharedPreference: SharedPreference = SharedPreference(App.instance)
    private lateinit var presenter: MainPresenter
    private var adapter: MainRvAdapter? = null

    var REQUIRED_PERMISSIONS_SDK_29_AND_ABOVE = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()) {
                return
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        presenter = MainPresenter(this)

        vb.locationItemsRv.layoutManager = LinearLayoutManager(this)
        adapter = MainRvAdapter(presenter, this)
        vb.locationItemsRv.adapter = adapter

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

        vb.helpButton.setOnClickListener {
            showHelp()
        }

        vb.ch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                helpCheckListDone++
                checkCheckList()
            } else {
                helpCheckListDone--
                checkCheckList()
            }
        }

        vb.ch2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                helpCheckListDone++
                checkCheckList()
            } else {
                helpCheckListDone--
                checkCheckList()
            }
        }

        vb.ch3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                helpCheckListDone++
                checkCheckList()
            } else {
                helpCheckListDone--
                checkCheckList()
            }
        }

        vb.ch4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                helpCheckListDone++
                checkCheckList()
            } else {
                helpCheckListDone--
                checkCheckList()
            }
        }

        if(sharedPreference.getValueInt("help_status")!=4){
            helpCheckListDone = 0
            sharedPreference.saveInt("help_status",0)
            showHelp()
        } else {
            helpCheckListDone = 4
            hideHelp()
        }
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()

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

    private fun checkCheckList(){
        if(helpCheckListDone == 4){
            hideHelp()
        }
        sharedPreference.saveInt("help_status",helpCheckListDone)
    }

    private fun showHelp(){
        vb.ch1.isChecked = false
        vb.ch2.isChecked = false
        vb.ch3.isChecked = false
        vb.ch4.isChecked = false
        helpCheckListDone = 0
        sharedPreference.saveInt("help_status",helpCheckListDone)
        vb.mainHelpLayout.visibility = View.VISIBLE
    }

    private fun hideHelp(){
        vb.mainHelpLayout.visibility = View.GONE
    }

    override fun backPressed(): Boolean {
        presenter.backPressed()
        return true
    }

    override fun onItemToolButtonClick(itemId: Long) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(itemId: Long) {
        TODO("Not yet implemented")
    }


}