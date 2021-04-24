package com.shubhampandey.newsonthego.network

import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    // Define end point
    @GET("news")
    fun getNews(
        // Parameter name
        // Eg. https://jsonplaceholder.typicode.com/posts?id=2
        @Query("access_key")
        accessKey: String,
        @Query("countries")
        country: String?,
        @Query("limit")
        fetchLimit: Int?,
        @Query("languages")
        language: String?
    ): retrofit2.Call<ResponseDataClass>
}