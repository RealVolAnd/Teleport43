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

    @Query("SELECT COUNT(id) FROM locationitems WHERE itemName =:itemName")
    fun getItemCountByName(itemName: String): Int

    @Query("UPDATE locationitems SET itemHitCount = itemHitCount+1 WHERE id =:itemId")
    fun incrItemHitCountById(itemId: Long)

    @Query("UPDATE locationitems SET itemName = :itemName,itemTrack = :itemTrack  WHERE id =:itemId")
    fun updateItemById(itemId: Long,itemName: String,itemTrack: String)

    @Query("DELETE FROM locationitems WHERE id =:itemId")
    fun deleteItemById(itemId: Long)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(locationItem: LocationItem)
}