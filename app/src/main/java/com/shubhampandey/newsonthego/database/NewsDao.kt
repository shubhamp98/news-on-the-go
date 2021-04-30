package com.shubhampandey.newsonthego.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shubhampandey.newsonthego.dataclass.NewsDataClass


@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    suspend fun getAllNews(): List<NewsDataClass>

    @Insert
    suspend fun insertNewsDetails(news: NewsDataClass)

    @Delete
    suspend fun deleteNewsDetails(news: NewsDataClass)
}
