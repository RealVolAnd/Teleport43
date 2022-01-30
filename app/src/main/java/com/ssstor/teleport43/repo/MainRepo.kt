package com.ssstor.teleport43.repo

import com.ssstor.teleport43.App
import com.ssstor.teleport43.DEFAULT_LOCATION_ITEM
import com.ssstor.teleport43.database.entities.LocationItem

object MainRepo {

 fun insertNewItem(item:LocationItem){
     Thread{
         App.db.locationItemDao.insert(item)
     }.start()
 }

    fun getItemById(itemId:Long):LocationItem{
        var result = DEFAULT_LOCATION_ITEM
        val t:Thread = Thread{
            result = App.db.locationItemDao.getItemById(itemId)
        }
        t.start()
        t.join()
        return result
    }

    fun addHitToItemById(itemId:Long){
        Thread{
        App.db.locationItemDao.incrItemHitCountById(itemId)
        }.start()
    }
}