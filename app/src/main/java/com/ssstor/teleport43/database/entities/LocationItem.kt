package com.ssstor.teleport43.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "locationitems",
    indices = [Index(value = ["itemName"], unique = true)]
)
class LocationItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val locationItemId: Long,

    @ColumnInfo(name = "itemName")
    val locationItemName: String,

    @ColumnInfo(name = "itemTrack")
    val locationItemTrack: String,

    @ColumnInfo(name = "itemHitCount")
    val locationItemHitCount: Int
)