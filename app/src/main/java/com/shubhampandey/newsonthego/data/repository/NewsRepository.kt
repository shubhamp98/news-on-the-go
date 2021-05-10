package com.shubhampandey.newsonthego.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.data.database.RoomDatabaseBuilder
import com.shubhampandey.newsonthego.data.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.data.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.data.network.ApiClient
import com.shubhampandey.newsonthego.utils.SharedPrefUtil
import retrofit2.Callback
import retrofit2.Response

class NewsRepository(context: Context) {

    private val TAG: String = NewsRepository::class.java.simpleName
    val newsResponsesFromNetworkLiveData: MutableLiveData<ResponseDataClass?> =
        MutableLiveData<ResponseDataClass?>()
    val newsResponsesFromDBLiveData: MutableLiveData<List<NewsDataClass>> =
        MutableLiveData<List<NewsDataClass>>()
    private val dbBuilder = RoomDatabaseBuilder.getInstance(context)


    // API parameters which will never change
    // and will remain same for each API call
    private val accessKey = context.getString(R.string.mediastacknews_access_key)
    private val country = SharedPrefUtil.getCountryFromPref(context)!!
    private val fetchLimit = null
    private val language = SharedPrefUtil.getLanguageFromPref(context)!!
    private val sortBy = context.getString(R.string.default_sort_order)

    /**
     * Call network model to fetch the news
     */
    fun getNewsFromNetwork(
        category: String?, searchKeyword: String?
    ) {
        val call = ApiClient.getClient.getNews(
            accessKey,
            category,
            country,
            searchKeyword,
            fetchLimit,
            language,
            sortBy
        )
        call.enqueue(object : Callback<ResponseDataClass> {
            override fun onFailure(call: retrofit2.Call<ResponseDataClass>, t: Throwable) {
                //Log.d(TAG, "Error is ${t.message}")
                newsResponsesFromNetworkLiveData.postValue(null)
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                //Log.d(TAG, "Data is ${response.body()}")
                if (response.isSuccessful) {
                    newsResponsesFromNetworkLiveData.postValue(response.body())
                } else {
                    newsResponsesFromNetworkLiveData.postValue(null)

                }
            }
        })
    }

    /**
     * Fetch saved news from Room DB using Coroutine [suspend]
     */
    suspend fun getNewsFromDB() {
        val newsList = dbBuilder.newsDao().getAllNews()
        //Log.d(TAG, "Data is $newsList")
        // get data from Database
        newsResponsesFromDBLiveData.postValue(newsList)
    }

    /**
     * Save news item to local database
     */
    suspend fun insertNewsToDB(news: NewsDataClass) {
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

    suspend fun removeNewsFromDB(news: NewsDataClass) {
        dbBuilder.newsDao().deleteNewsDetails(news)
    }
}