package com.ssstor.teleport43.repo

import com.ssstor.teleport43.App
import com.ssstor.teleport43.DEFAULT_LOCATION_ITEM
import com.ssstor.teleport43.database.entities.LocationItem
import com.ssstor.teleport43.database.entities.LocationSettings

object MainRepo {

 fun insertNewItem(item:LocationItem){
     Thread{
         App.db.locationItemDao.insert(item)
     }.start()
 }

    fun updateItem(itemId:Long, itemName:String, itemTrack: String){
        Thread{
            App.db.locationItemDao.updateItemById(itemId, itemName, itemTrack)
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

    fun getItemCountByName(itemName:String):Int{
        var result = 0
        val t:Thread = Thread{
            result = App.db.locationItemDao.getItemCountByName(itemName)
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

    fun deleteItemById(itemId:Long){
        Thread{
            App.db.locationItemDao.deleteItemById(itemId)
        }.start()
    }

    //Settings....
    fun getSettingsCountByKey(key: String): Int{
        var result = 0
        val t:Thread = Thread{
            result =  App.db.locationSettingsDao.getSettingsCountByKey(key)
        }
        t.start()
        t.join()
        return result
    }

    fun getSettingsValueByKey(key: String): String{
        var result = ""
        val t:Thread = Thread{
            result =  App.db.locationSettingsDao.getSettingsValueByKey(key)
        }
        t.start()
        t.join()
        return result
    }

    fun setSettingsValueByKey(key: String,value: String){
        Thread{
            App.db.locationSettingsDao.setSettingsValueByKey(key,value)
        }.start()
    }

    fun insertSettings(locationSettings: LocationSettings){
        Thread{
            App.db.locationSettingsDao.insert(locationSettings)
        }.start()
    }
}