package com.ssstor.teleport43

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

val Context.app: App
    get() = applicationContext as App