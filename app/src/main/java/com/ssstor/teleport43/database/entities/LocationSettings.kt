package com.ssstor.teleport43.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "locationsettings",
    indices = [Index(value = ["keyString"], unique = true)]
)
class LocationSettings(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val timerSettingsId: Long,

    @ColumnInfo(name = "keyString")
    val timerSettingsKey: String,

    @ColumnInfo(name = "valueString")
    val timerSettingsValue: String
)