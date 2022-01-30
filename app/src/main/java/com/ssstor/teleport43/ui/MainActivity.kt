package com.ssstor.teleport43.ui

import android.Manifest
import android.app.Service
import android.app.role.RoleManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.ssstor.teleport43.*
import com.ssstor.teleport43.BROADCAST_CLOSE_APP
import com.ssstor.teleport43.databinding.ActivityMainBinding
import com.ssstor.teleport43.repo.MainRepo
import com.ssstor.teleport43.services.MockLocationProvider
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
        pm = getSystemService(POWER_SERVICE) as PowerManager

        vb.locationItemsRv.layoutManager = LinearLayoutManager(this)
        adapter = MainRvAdapter(presenter, this)
        vb.locationItemsRv.adapter = adapter

        initViews()
        checkPermissions()

        setBroadcastReceiver()
        checkStatus()
        startMyService()
    }

    override fun onDestroy() {
       // if(App.hasTrouble) stopMyService()
        super.onDestroy()
    }

    fun setAppAsMockProvider() {
        startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
    }

    private fun checkPermissions(){
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                REQUIRED_PERMISSIONS_SDK_29_AND_ABOVE,
                REQUEST_CODE
            )
            App.isLocationPermission = false
        } else {
            App.isLocationPermission = true
            setPowerMode()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
               // App.isLocationPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

                return
            }
        }
    }

    private fun  initViews(){

        vb.appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when (state) {
                    State.COLLAPSED -> {
                        vb.currentItemText.visibility = View.VISIBLE
                    }
                    State.EXPANDED -> {
                    }

                    State.IDLE -> {
                    }
                }
            }
        })

        vb.itemAddFab.setOnClickListener {
            showEditItemDialog(FLAG_ADD_ITEM,0)
        }

        vb.closeButton.setOnClickListener {
            stopMyService()
        }

        vb.itemCancelButton.setOnClickListener {
            hideEditItemDialog()
        }

        vb.itemSaveButton.setOnClickListener {
            presenter.saveItem(vb.itemNameText.text.toString(),vb.itemTrackText.text.toString())
            hideEditItemDialog()
        }
    }

    private fun  showEditItemDialog(flag:Int,itemId:Long){
        when(flag){
            FLAG_ADD_ITEM ->{
                vb.itemEditTitle.text = getString(R.string.new_item)
                vb.itemEditLayout.visibility = View.VISIBLE
            }
            FLAG_EDIT_ITEM ->{
                vb.itemEditTitle.text = getString(R.string.edit_item)
                val tmpItem = MainRepo.getItemById(itemId)
                vb.itemNameText.setText(tmpItem.locationItemName)
                vb.itemTrackText.setText(tmpItem.locationItemTrack)
                vb.itemEditLayout.visibility = View.VISIBLE
            }
        }
        vb.itemAddFab.visibility = View.GONE
    }

    private fun  hideEditItemDialog(){
        vb.itemNameText.setText("")
        vb.itemTrackText.setText("")
        vb.itemEditLayout.visibility = View.GONE
        vb.itemAddFab.visibility = View.VISIBLE
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()

    }

    private fun checkGps(){
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        App.isGpsOn = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        App.isNetGpsOff = !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun setPowerMode() {

        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            val intentIgnoreBattery = Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.parse("package:$packageName")
            )
            startActivity(intentIgnoreBattery)
        }
    }

    private fun checkPowerMode() {
        App.isPowerModeSet = pm.isIgnoringBatteryOptimizations(packageName)
        val d = 0
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



    private fun hideHelp(){
        vb.mainHelpLayout.visibility = View.GONE
    }

    override fun backPressed(): Boolean {
        presenter.backPressed()
        return true
    }

    override fun onItemToolButtonClick(itemId: Long) {
       showEditItemDialog(FLAG_EDIT_ITEM,itemId)
    }

    override fun onItemClick(itemId: Long) {
        setActiveItem(itemId)
    }

    private fun setActiveItem(itemId: Long){
        val tmpItem = MainRepo.getItemById(itemId)
        App.CURRENT_ITEM = tmpItem
        App.locationChanged = true
        vb.currentItemText.text = tmpItem.locationItemName
        MainRepo.addHitToItemById(itemId)
    }

    private fun checkMock(){
        val lm = getSystemService(Service.LOCATION_SERVICE) as LocationManager
        val mock_gps: MockLocationProvider = MockLocationProvider(LocationManager.GPS_PROVIDER)

    }

    private fun checkStatus(){
        Thread{
            while(true){
                checkGps()
                checkPermissions()
               // checkPowerMode()
              //  checkMock()

                Thread.sleep(CHECK_STATUS_INTERVAL_IN_MS)

                var totalStatus = false

                if(App.isGpsOn) {
                    vb.helpGpsLayout.background.setTint(resources.getColor(R.color.noalert_color))
                }else {
                    vb.helpGpsLayout.background.setTint(resources.getColor(R.color.alert_color))
                    totalStatus = true
                }

                if(App.isNetGpsOff) {
                    vb.helpGoogleLayout.background.setTint(resources.getColor(R.color.noalert_color))
                }else {
                    vb.helpGoogleLayout.background.setTint(resources.getColor(R.color.alert_color))
                    totalStatus = true
                }

                if(App.isLocationPermission) {
                    vb.helpPermissionsLayout.background.setTint(resources.getColor(R.color.noalert_color))
                }else {
                    vb.helpPermissionsLayout.background.setTint(resources.getColor(R.color.alert_color))
                    totalStatus = true
                }

                if(App.isMockDefault) {
                    vb.helpMockLayout.background.setTint(resources.getColor(R.color.noalert_color))
                }else {
                    vb.helpMockLayout.background.setTint(resources.getColor(R.color.alert_color))
                    totalStatus = true
                }
/*
                if(App.isPowerModeSet) {
                    //vb.helpPermissionsLayout.background.setTint(resources.getColor(R.color.noalert_color))
                }else {
                    vb.helpPermissionsLayout.background.setTint(resources.getColor(R.color.alert_color))
                    totalStatus = true
                }
*/
                if(totalStatus) showStatus() else hideStatus()
            }


        }.start()
    }

    private fun showStatus(){
       runOnUiThread{
               vb.mainHelpLayout.visibility = View.VISIBLE
        }

    }
    private fun hideStatus(){
        runOnUiThread {
            vb.mainHelpLayout.visibility = View.GONE
        }
    }


}