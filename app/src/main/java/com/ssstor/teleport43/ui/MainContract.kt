package com.ssstor.teleport43.ui

interface MainContract {
    interface View {
        fun updateList()
    }

    interface Presenter {
        fun backPressed(): Boolean
        fun saveItem(itemId:Long, itemName: String, itemTrack: String)
        fun deleteItem(itemId: Long)
    }
}