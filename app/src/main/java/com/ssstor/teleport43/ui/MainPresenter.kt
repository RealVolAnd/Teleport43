package com.ssstor.teleport43.ui

import com.ssstor.teleport43.App
import com.ssstor.teleport43.database.entities.LocationItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainPresenter(private val view: MainContract.View): MainContract.Presenter {
    val itemList = mutableListOf<LocationItem>()
init{
    App.db.locationItemDao.getAllCallLogCalls().subscribeOn(Schedulers.io())
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






    fun getCount() = itemList.size


    override fun backPressed(): Boolean {
        return true
    }


}