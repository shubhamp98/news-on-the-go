package com.shubhampandey.newsonthego.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.data.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.data.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.data.repository.NewsRepository
import com.shubhampandey.newsonthego.utils.SharedPrefUtil
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = NewsViewModel::class.java.simpleName
    private val context = getApplication<Application>().applicationContext
    private val newsRepository: NewsRepository = NewsRepository(context)
    val newsResponsesLiveData: LiveData<ResponseDataClass?> =
        newsRepository.newsResponsesFromNetworkLiveData
    val bookmarkedNewsLiveData = newsRepository.newsResponsesFromDBLiveData

    /**
     * Get news from API/Network
     */
    fun getNewsFromAPI(
        category: String?, searchKeyword: String?
    ) {
        newsRepository.getNewsFromNetwork(
            category,
            searchKeyword
        )
    }

    fun getNewsFromDB() {
        // Using Coroutine
        viewModelScope.launch {
            try {
                newsRepository.getNewsFromDB()
            } catch (e: Exception) {
                // handler error
                Log.e(TAG, e.localizedMessage!!)
            }
        }
    }

    fun saveNewsToDB(news: NewsDataClass) {
        viewModelScope.launch {
            try {
                newsRepository.insertNewsToDB(news)
            } catch (e: Exception) {
                // handler error
                Log.e(TAG, e.localizedMessage!!)
            }
        }
    }

    fun deleteNewsFromDB(news: NewsDataClass) {
        viewModelScope.launch {
            try {
                newsRepository.removeNewsFromDB(news)
            } catch (e: Exception) {
                // handler error
                Log.e(TAG, e.localizedMessage!!)
            }
        }
    }
}