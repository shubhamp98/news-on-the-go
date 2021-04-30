package com.shubhampandey.newsonthego.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shubhampandey.newsonthego.MyApplication
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.utils.SharedPrefUtil
import com.shubhampandey.newsonthego.ui.adapter.NewsAdapter
import com.shubhampandey.newsonthego.data.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.ui.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_display_search_news.*
import kotlinx.android.synthetic.main.no_internet.*

class SearchNewsFragment : Fragment() {

    private val TAG = SearchNewsFragment::class.java.simpleName
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

        if (hasNetworkConnectivity()) {
            tryConnectivity()
        }
        else {
            showInternetConnectivityError()
            hideList()
            search_frag_no_connection_Layout.visibility = View.VISIBLE
            noConnectionRetry_Btn.setOnClickListener {
                tryConnectivity()
            }
        }
    }

    private fun tryConnectivity() {
        if (hasNetworkConnectivity()) {
            setupUI()
            search_frag_no_connection_Layout.visibility = View.GONE
        }
        else {
            showInternetConnectivityError()
        }
    }

    private fun showInternetConnectivityError() {
        Snackbar.make(requireView(), "Internet connectivity failed", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Check for Internet connection
     */
    private fun hasNetworkConnectivity() =
        MyApplication().hasNetwork()

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
            country = SharedPrefUtil.getCountryFromPref(requireActivity())!!,
            searchKeyword = searchedKeywords,
            fetchLimit = null,
            language = SharedPrefUtil.getLanguageFromPref(requireActivity())!!,
            sort = getString(
                R.string.default_sort_order
            )
        )
        newsViewModel.newsResponsesLiveData.observe(viewLifecycleOwner, Observer {
            //Log.i(TAG, "Data is ${it?.newsData}")
            if (it != null) {
                if (it.newsData.isNotEmpty()) {
                    search_frag_no_info_layout.visibility = View.GONE
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
                    search_frag_no_info_layout.visibility = View.VISIBLE
                }

            } else {
                // Handle your errors here
                showError()
                hideAnimatedLoader()
                searchNewsList_RV.visibility = View.GONE
                search_frag_no_info_layout.visibility = View.VISIBLE
            }
        })

    }

    private fun showError() {
        Snackbar.make(
            requireContext(),
            requireView(),
            "Something went wrong",
            Snackbar.LENGTH_SHORT
        ).show()
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
                    Log.i(TAG, "Search query: $query")
                    showAnimatedLoader()
                    // Fetch the News
//                    getDemoLiveNews()
                    getSearchedNews(query)
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

    /**
     * Make recyclerview invisible
     */
    private fun hideList() {
        searchNewsList_RV.visibility = View.INVISIBLE
    }

    private fun showAnimatedLoader() {
        search_frag_loading_layout.visibility = View.VISIBLE
    }

    private fun hideAnimatedLoader() {
        search_frag_loading_layout.visibility = View.GONE
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