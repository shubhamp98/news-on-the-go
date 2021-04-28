package com.shubhampandey.newsonthego.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.network.ApiClient
import com.shubhampandey.newsonthego.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_display_category_news.*
import kotlinx.android.synthetic.main.fragment_display_live_news.*
import kotlinx.android.synthetic.main.fragment_display_search_news.*
import retrofit2.Callback
import retrofit2.Response

class SearchNewsFragment : Fragment() {

    private val TAG = SearchNewsFragment::class.java.simpleName
    lateinit var progressDialog: ProgressDialog
    var newsDataset = ArrayList<NewsDataClass>()
    private lateinit var customNewsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_search_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    /**
     * Call functions to setup the UI
     */
    private fun setupUI() {
        addFocusAndOpenKeyBoard()
        setupListener()
        setupRecyclerView()
    }

    /**
     * Fetch news by searched keywords by calling the news API
     */
    /**
     * Fetch live news by calling the news API
     */
    private fun getSearchedNews(searchedKeywords: String) {
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.getNewsFromAPI(
            accessKey = getString(R.string.mediastacknews_access_key),
            category = null,
            country = getString(R.string.default_country_india),
            searchKeyword = searchedKeywords,
            fetchLimit = null,
            language = getString(R.string.default_language_english),
            sort = getString(
                R.string.default_sort_order
            )
        )
        newsViewModel.newsResponsesLiveData.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "Data is ${it?.newsData}")
            if (it != null) {
                if (it.newsData.isNotEmpty()) {
                    newsDataset.clear()
                    newsDataset.addAll(it.newsData)
                    searchNewsList_RV.adapter!!.notifyDataSetChanged()
                    // Make news view visible and hide others
                    updateUI()
                }
                else {
                    // Data received is empty
                    hideAnimatedLoader()
                    searchNewsList_RV.visibility = View.GONE
                    searchNewsInfo_TV.text = "No news found for your searched term..."
                    searchNewsInfo_TV.visibility = View.VISIBLE
                }

            } else {
                // Handle your errors here
                Log.i(TAG, "No Data Found")
            }
        })

    }

    /**
     * Add focus to Search view and
     * show the keyboard
     */
    private fun addFocusAndOpenKeyBoard() {
        searchNews_SV.requestFocus()
        val imm: InputMethodManager = context
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchNews_SV, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    /**
     * Hide the keyboard when view is destroyed
     */
    private fun hideKeyboard() {
        val imm: InputMethodManager = context
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    /**
     * Add listeners to views
     */
    private fun setupListener() {
        searchNews_SV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNews_SV.clearFocus()
                if (query != null) { // Check if search query is not null
                    showAnimatedLoader()
                    searchNewsInfo_TV.visibility = View.GONE
                    // Fetch the News
//                    getDemoLiveNews()
                    getSearchedNews(query)
                } else {
                    showEmptySearchError()
                }
                // Returning false will hide the keyboard
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    /**
     * Make recyclerview visible
     */
    private fun showList() {
        searchNewsList_RV.visibility = View.VISIBLE
    }


    private fun showAnimatedLoader() {
        searchNews_LAV.visibility = View.VISIBLE
    }

    private fun hideAnimatedLoader() {
        searchNews_LAV.visibility = View.GONE
    }

    private fun showEmptySearchError() {
        Snackbar.make(
            requireContext(),
            requireView(),
            "Search query must need to be entered!",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * Make view visible which shows the news
     * after fetching and hide other views
     */
    private fun updateUI() {
        hideAnimatedLoader()
        showList()
    }

    private fun getDemoLiveNews() {
        for (i in 0..10) {
            newsDataset.add(
                NewsDataClass(
                    null,
                    "This is dummy title of News. This is dummy title of News",
                    "This is a dummy new description for UI purpose. This is dummy title of News. This is dummy title of News",
                    "https://static.vecteezy.com/system/resources/previews/000/228/631/non_2x/vector-news-background-with-text-live-updates.jpg",
                    "24-04-2021",
                    "Dummy Source",
                    "hello"
                )
            )
        }
        searchNewsList_RV.adapter!!.notifyDataSetChanged()
        showList()
        hideAnimatedLoader()
    }

    /**
     * Setup the recycler view and attach adapter to it
     */
    private fun setupRecyclerView() {
        // Set layout for RecyclerView
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchNewsList_RV.layoutManager = linearLayoutManager
        customNewsAdapter = context?.let { NewsAdapter(it, newsDataset) }!!
        // attach adapter
        searchNewsList_RV.adapter = customNewsAdapter
    }
}