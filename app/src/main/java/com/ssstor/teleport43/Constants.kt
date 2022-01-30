package com.ssstor.teleport43

import com.ssstor.teleport43.database.entities.LocationItem

val DB_NAME = "t43_db"
val BASE_RESOURCES_PATH = "android.resource://com.ssstor.teleport43/"
val AUDIO_BLANK_PATH = BASE_RESOURCES_PATH + R.raw.silent

val ACTION = "SEND_DATA"


val BROADCAST_CLOSE_APP = 1

val BROADCAST_APPLICATION_STATE =1

val ITEM_TYPE_TRACK = "Track"
val ITEM_TYPE_POINT = "Point"

val DEFAULT_LOCATION_ITEM = LocationItem(0,"Default","14.7896:-15.9075:10",0)
