package com.shubhampandey.newsonthego.data.network

import com.shubhampandey.newsonthego.data.dataclass.ResponseDataClass
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
        @Query("categories")
        category: String?,
        @Query("countries")
        country: String?,
        @Query("keywords")
        searchKeyword: String?,
        @Query("limit")
        fetchLimit: Int?,
        @Query("languages")
        language: String?,
        @Query("sort")
        sort: String?
    ): retrofit2.Call<ResponseDataClass>
}