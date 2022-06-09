package com.ssstor.teleport43

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ssstor.teleport43.database.entities.LocationItem
import com.ssstor.teleport43.database.entities.LocationItemsDatabase

class App : Application() {

    companion object {
        lateinit var instance: App
        lateinit var db: LocationItemsDatabase
        val ALT:Double = 16.0
        var CURRENT_ITEM: LocationItem = DEFAULT_LOCATION_ITEM
        var locationChanged = true
        var isGpsOn = false
        var isMockDefault = false
        var isLocationPermission = false
        var isNetGpsOff = false
        var isPowerModeSet = false
    }
    /*
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE timerchains ADD COLUMN closeApp INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE timerchains ADD COLUMN collectStatistics INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE timers ADD COLUMN collectStatistics INTEGER NOT NULL DEFAULT 0")
        }
    }
*/
    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(this, LocationItemsDatabase::class.java, DB_NAME)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
           // .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    fun showMessage(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    fun itemStringToList(itemString: String): List<String>{
        var result= itemString.split(";")
        return result
    }
}



val Context.app: App
    get() = applicationContext as App