package com.ssstor.teleport43.ui

import com.ssstor.teleport43.App
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
    override fun saveItem(itemName: String, itemTrack: String) {
        if(itemName.isNotEmpty()&&itemTrack.isNotEmpty()){
            MainRepo.insertNewItem(LocationItem(0,itemName,itemTrack,0))
        } else {
            App.instance.showMessage("Item name and track must have text value")
        }

    }


    fun getCount() = itemList.size


    override fun backPressed(): Boolean {
        return true
    }




}