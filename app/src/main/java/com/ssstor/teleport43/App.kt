package com.ssstor.teleport43

import android.app.Application
import android.content.Context
import android.widget.Toast

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun showMessage(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }
}



val Context.app: App
    get() = applicationContext as App