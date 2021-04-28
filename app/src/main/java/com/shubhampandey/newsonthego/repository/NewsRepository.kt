package com.shubhampandey.newsonthego.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shubhampandey.newsonthego.database.RoomDatabaseBuilder
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.network.ApiClient
import kotlinx.android.synthetic.main.fragment_bookmarked_news.*
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class NewsRepository(context: Context) {

    private val TAG = NewsRepository::class.java.simpleName
    val newsResponsesFromNetworkLiveData: MutableLiveData<ResponseDataClass?> =
        MutableLiveData<ResponseDataClass?>()
    val newsResponsesFromDBLiveData: MutableLiveData<List<NewsDataClass>> =
        MutableLiveData<List<NewsDataClass>>()
    private val dbBuilder = RoomDatabaseBuilder.getInstance(context)


    /**
     * Call network model to fetch the news
     */
    fun getNewsFromNetwork(
        accessKey: String, category: String?, country: String, searchKeyword: String?,
        fetchLimit: Int?, language: String, sort: String
    ) {
        val call = ApiClient.getClient.getNews(
            accessKey,
            category,
            country,
            searchKeyword,
            fetchLimit,
            language,
            sort
        )
        call.enqueue(object : Callback<ResponseDataClass> {
            override fun onFailure(call: retrofit2.Call<ResponseDataClass>, t: Throwable) {
                Log.d(TAG, "Error is ${t.message}")
                newsResponsesFromNetworkLiveData.postValue(null)
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                //Log.d(TAG, "Data is ${response.body()}")
                newsResponsesFromNetworkLiveData.postValue(response.body())
            }
        })
    }

    /**
     * Fetch saved news from Room DB
     */
    fun getNewsFromDB() {
        Executors.newSingleThreadExecutor().execute {
            val newsList = dbBuilder.newsDao().getAllNews()
            Log.d(TAG, "Data is $newsList")
            // get data from Database
            newsResponsesFromDBLiveData.postValue(newsList.value)
        }
    }

    /**
     * Save news item to local database
     */
    fun insertNewsToDB(news: NewsDataClass) {
        Executors.newSingleThreadExecutor().execute {
            dbBuilder.newsDao().insertNewsDetails(
                NewsDataClass(
                    news.id,
                    news.newsTitle,
                    news.newsDescription,
                    news.newsThumbnailImageURL,
                    news.newsPublishedAt,
                    news.newsSource,
                    news.newsURL
                )
            )
        }
    }
}