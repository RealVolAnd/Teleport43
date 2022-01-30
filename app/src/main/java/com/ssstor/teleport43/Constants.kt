package com.ssstor.teleport43

import com.ssstor.teleport43.database.entities.LocationItem

val COORDINATES_REFRESH_INTERVAL_IN_MS = 1000L

val DB_NAME = "t43_db"
val BASE_RESOURCES_PATH = "android.resource://com.ssstor.teleport43/"
val AUDIO_BLANK_PATH = BASE_RESOURCES_PATH + R.raw.silent

val ACTION = "SEND_DATA"

val FLAG_ADD_ITEM = 1
val FLAG_EDIT_ITEM = 2

val BROADCAST_CLOSE_APP = 1

val BROADCAST_APPLICATION_STATE =1

val ITEM_TYPE_TRACK = "Track"
val ITEM_TYPE_POINT = "Point"

val DEFAULT_LOCATION_ITEM = LocationItem(0,"Default","36.42,3.08",0)
