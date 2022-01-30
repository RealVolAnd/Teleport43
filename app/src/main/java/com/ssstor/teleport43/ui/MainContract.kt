package com.ssstor.teleport43.ui

interface MainContract {
    interface View {
        fun updateList()
    }

    interface Presenter {
        fun backPressed(): Boolean
    }
}