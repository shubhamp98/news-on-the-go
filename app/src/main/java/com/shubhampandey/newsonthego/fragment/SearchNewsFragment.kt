package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.network.ApiClient
import kotlinx.android.synthetic.main.fragment_display_category_news.*
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
        createProgressDialog()
        setupRecyclerView()
    }

    /**
     * Fetch news by searched keywords by calling the news API
     */
    private fun getSearchedNews(searchedKeywords: String) {
        showProgressDialog()
        val call = ApiClient.getClient.getNews(
            accessKey = getString(R.string.mediastacknews_access_key),
            category = null,
            country = getString(R.string.default_country_india),
            searchKeyword = searchedKeywords,
            fetchLimit = null,
            language = getString(R.string.default_language_english),
            sort = getString(R.string.default_sort_order)
        )
        call.enqueue(object : Callback<ResponseDataClass> {
            override fun onFailure(call: retrofit2.Call<ResponseDataClass>, t: Throwable) {
                //Log.d(TAG, "Error is ${t.message}")
                Toast.makeText(
                    context,
                    getString(R.string.live_news_fetching_error_msg),
                    Toast.LENGTH_SHORT
                ).show()
                dismissProgressDialog()
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                response.body()?.newsData?.let { newsDataset.addAll(it) }
                searchNewsList_RV.adapter!!.notifyDataSetChanged()
                dismissProgressDialog()
                //Log.d(TAG, "Data is ${response.body()}")
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

    /**
     * Add listeners to views
     */
    private fun setupListener() {
        searchNews_SV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNews_SV.clearFocus()
                if (query != null) { // Check if search query is not null
                    // Fetch the News
                    //getDemoLiveNews()
                    getSearchedNews(query)
                    // Make news view visible and hide others
                    updateUI()
                }
                else {
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

    private fun showEmptySearchError() {
        Snackbar.make(requireContext(), requireView(), "Search query must need to be entered!", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Make view visible which shows the news
     * after fetching and hide other views
     */
    private fun updateUI() {
        searchNewsInfo_TV.visibility = View.GONE
        searchNewsList_RV.visibility = View.VISIBLE
    }

    private fun getDemoLiveNews() {
        showProgressDialog()
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
        dismissProgressDialog()
    }

    /**
     * Create progress dialog to update the user
     * that news is getting loaded
     */
    private fun createProgressDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait while we are fetching news...")
        progressDialog.setCancelable(false)
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

    /**
     * Show the progress dialog
     */
    private fun showProgressDialog(){
        progressDialog.show()
    }

    /**
     * Hide the progress dialog
     */
    private fun dismissProgressDialog() {
        progressDialog.dismiss()
    }
}