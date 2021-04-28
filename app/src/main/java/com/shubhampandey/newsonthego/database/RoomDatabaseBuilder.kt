package com.shubhampandey.newsonthego.database

import android.content.Context
import androidx.room.Room

object RoomDatabaseBuilder {
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE =
                    buildRoomDB(
                        context
                    )
            }
        }
        return INSTANCE!!
    }

    // Creating database using Room
    private fun buildRoomDB(context: Context): AppDatabase? {
        return Room.databaseBuilder(context, AppDatabase::class.java, "news_db")
            .fallbackToDestructiveMigration().build()
    }

}