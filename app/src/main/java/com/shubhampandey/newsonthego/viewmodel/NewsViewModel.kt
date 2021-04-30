package com.shubhampandey.newsonthego.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = NewsViewModel::class.java.simpleName
    private val context = getApplication<Application>().applicationContext
    private val newsRepository: NewsRepository = NewsRepository(context)
    val newsResponsesLiveData: LiveData<ResponseDataClass?> =
        newsRepository.newsResponsesFromNetworkLiveData
    val bookmarkedNewsLiveData = newsRepository.newsResponsesFromDBLiveData

    fun getNewsFromAPI(
        accessKey: String, category: String?, country: String, searchKeyword: String?,
        fetchLimit: Int?, language: String, sort: String
    ) {
        newsRepository.getNewsFromNetwork(
            accessKey,
            category,
            country,
            searchKeyword,
            fetchLimit,
            language,
            sort
        )
    }

    fun getNewsFromDB() {
        // Using Coroutine
        viewModelScope.launch {
            try {
                newsRepository.getNewsFromDB()
            }
            catch (e: Exception) {
                // handler error
                Log.e(TAG, e.localizedMessage!!)
            }
        }
    }

    fun saveNewsToDB(news: NewsDataClass) {
        viewModelScope.launch {
            try {
                newsRepository.insertNewsToDB(news)
            }
            catch (e: Exception) {
                // handler error
                Log.e(TAG, e.localizedMessage!!)
            }
        }
    }

    fun deleteNewsFromDB(news: NewsDataClass) {
        viewModelScope.launch {
            try {
                newsRepository.removeNewsFromDB(news)
            }
            catch (e: Exception) {
                // handler error
                Log.e(TAG, e.localizedMessage!!)
            }
        }
    }
}