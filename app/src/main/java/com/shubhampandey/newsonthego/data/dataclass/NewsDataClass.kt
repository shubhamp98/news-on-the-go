package com.shubhampandey.newsonthego.data.dataclass


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class NewsDataClass(
    @PrimaryKey(autoGenerate = true)
    // Using @ColumnInfo we are giving custom
    // column name of table
    @ColumnInfo(name = "news_item_id")
    var id: Int? = 0,
    @SerializedName("title")
    val newsTitle: String,
    @SerializedName("description")
    val newsDescription: String,
    @SerializedName("image")
    val newsThumbnailImageURL: String,
    @SerializedName("published_at")
    val newsPublishedAt: String,
    @SerializedName("source")
    val newsSource: String,
    @SerializedName("url")
    val newsURL: String
)
