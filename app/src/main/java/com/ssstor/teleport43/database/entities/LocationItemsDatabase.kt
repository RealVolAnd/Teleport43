package com.ssstor.teleport43.database.entities

import androidx.room.RoomDatabase
import com.ssstor.teleport43.database.dao.LocationItemDao
import com.ssstor.teleport43.database.dao.LocationSettingsDao


@androidx.room.Database(
    entities = [LocationItem::class, LocationSettings::class],
    version = 1
)
abstract class LocationItemsDatabase : RoomDatabase() {
    abstract val locationItemDao: LocationItemDao
    abstract val locationSettingsDao: LocationSettingsDao

    companion object {

        private var instance: LocationItemsDatabase? = null

        fun getInstance() = instance
            ?: throw RuntimeException("Database has not been created. Please call create(context)")
    }
}