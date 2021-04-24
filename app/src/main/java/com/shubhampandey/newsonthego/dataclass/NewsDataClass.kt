package com.shubhampandey.newsonthego.dataclass

import com.google.gson.annotations.SerializedName

data class NewsDataClass(
    @SerializedName("title")
    val newsTitle: String,
    @SerializedName("description")
    val newsDescription: String,
    @SerializedName("image")
    val newsThumbnailImageURL: String,
    @SerializedName("published_at")
    val newsPublishedAt: String
)
