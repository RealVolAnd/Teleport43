package com.ssstor.teleport43.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssstor.teleport43.database.entities.LocationSettings

@Dao
interface LocationSettingsDao {

    @Query("SELECT * FROM locationsettings")
    fun getAllSettings(): List<LocationSettings>

    @Query("SELECT valueString FROM locationsettings WHERE keyString =:key")
    fun getSettingsValueByKey(key: String): String

    @Query("SELECT COUNT(id) FROM locationsettings WHERE keyString =:key")
    fun getSettingsCountByKey(key: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timerSettings: LocationSettings)

    @Query("UPDATE locationsettings set valueString =:value WHERE keyString =:key")
    fun setSettingsValueByKey(key: String, value: String)
}