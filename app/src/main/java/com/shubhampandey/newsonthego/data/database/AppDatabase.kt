package com.shubhampandey.newsonthego.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shubhampandey.newsonthego.data.dataclass.NewsDataClass

@Database(entities = [NewsDataClass::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun newsDao() : NewsDao
}
