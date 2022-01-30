package com.ssstor.teleport43.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssstor.teleport43.database.entities.LocationItem
import io.reactivex.Observable


@Dao
interface LocationItemDao {

    @Query("SELECT * FROM locationitems ORDER BY itemHitCount DESC")
    fun getAllItems(): Observable<List<LocationItem>>

    @Query("SELECT * FROM locationitems WHERE id =:itemId")
    fun getItemById(itemId: Long): LocationItem

    @Query("UPDATE locationitems SET itemHitCount = itemHitCount+1 WHERE id =:itemId")
    fun incrItemHitCountById(itemId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationItem: LocationItem)
}