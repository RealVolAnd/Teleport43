package com.ssstor.teleport43.ui

import com.ssstor.teleport43.App
import com.ssstor.teleport43.SETTINGS_KEY_CURRENT_LOCATION
import com.ssstor.teleport43.database.entities.LocationItem
import com.ssstor.teleport43.repo.MainRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainPresenter(private val view: MainContract.View): MainContract.Presenter {
    val itemList = mutableListOf<LocationItem>()
init{
    App.db.locationItemDao.getAllItems().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onNext = {
                itemList.clear()
                itemList.addAll(it)
                view.updateList()
            },
            onError = {

            })
}
    override fun saveItem(itemId:Long, itemName: String, itemTrack: String) {
        if(itemName.isNotEmpty()&&itemTrack.isNotEmpty()){
            if(itemId>0){
                MainRepo.updateItem(itemId,itemName, itemTrack)
                App.locationChanged =true
            } else {
               if(MainRepo.getItemCountByName(itemName)>0){
                   App.instance.showMessage("Error adding Location. Duplicate Name")
               } else {
                   MainRepo.insertNewItem(LocationItem(0,itemName,itemTrack,0))
               }
            }
        } else {
            App.instance.showMessage("Item's Name and Track must have text value")
        }

    }

    override fun deleteItem(itemId: Long) {
        if(MainRepo.getSettingsValueByKey(SETTINGS_KEY_CURRENT_LOCATION).toLong()!=itemId){
            MainRepo.deleteItemById(itemId)
        }else {
            App.instance.showMessage("Error occurred while delete Location. Location in use. Please change active Location first")
        }

    }

    fun getCount() = itemList.size


    override fun backPressed(): Boolean {
        return true
    }




}