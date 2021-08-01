package com.shubhampandey.livenewsonthego.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shubhampandey.livenewsonthego.data.dataclass.NewsDataClass


@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    suspend fun getAllNews(): List<NewsDataClass>

    @Insert
    suspend fun insertNewsDetails(news: NewsDataClass)

    @Delete
    suspend fun deleteNewsDetails(news: NewsDataClass)
}
