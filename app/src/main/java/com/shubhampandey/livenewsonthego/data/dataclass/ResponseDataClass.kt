package com.shubhampandey.livenewsonthego.data.dataclass

import com.google.gson.annotations.SerializedName

data class ResponseDataClass(
    @SerializedName("data")
    val newsData: List<NewsDataClass>
)
