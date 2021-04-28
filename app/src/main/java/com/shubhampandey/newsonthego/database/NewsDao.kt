package com.shubhampandey.newsonthego.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shubhampandey.newsonthego.dataclass.NewsDataClass


@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<NewsDataClass>>

    @Insert
    fun insertNewsDetails(news: NewsDataClass)
}
