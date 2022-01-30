package com.ssstor.teleport43.services

import android.app.Service
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import com.ssstor.teleport43.App
import com.ssstor.teleport43.R
import java.lang.Exception

class MockLocationProvider(private val providerName: String) {
    var lm = App.instance.getSystemService(Service.LOCATION_SERVICE) as LocationManager

    init {
        try{
            lm.addTestProvider(
                providerName, false, false, false, false, false,
                true, true, ProviderProperties.POWER_USAGE_HIGH, ProviderProperties.ACCURACY_COARSE
            )
            lm.setTestProviderEnabled(providerName, true)
            App.isMockDefault = true
        } catch (e:Exception){
            App.isMockDefault = false
          //  App.instance.showMessage(App.instance.getString(R.string.no_mock))
        }

    }

    fun pushLocation(lat: Double, lon: Double, alt: Double) {
        val lm = App.instance.getSystemService(Service.LOCATION_SERVICE) as LocationManager

        val mockLocation = Location(providerName)
        mockLocation.setLatitude(lat)
        mockLocation.setLongitude(lon)
        mockLocation.altitude = alt
        mockLocation.time = System.currentTimeMillis()
        mockLocation.accuracy = 16f
        mockLocation.bearingAccuracyDegrees =0f
        mockLocation.speedAccuracyMetersPerSecond = 0f
        mockLocation.verticalAccuracyMeters = 0f
        mockLocation.speed = 1.0f
        mockLocation.setElapsedRealtimeNanos(System.nanoTime())
        try{
            lm.setTestProviderLocation(providerName, mockLocation)
        } catch (e:Exception) {

        }
    }

    fun shutdown() {
        val lm = App.instance.getSystemService(Service.LOCATION_SERVICE) as LocationManager
        lm.removeTestProvider(providerName)
    }
}