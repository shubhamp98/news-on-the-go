package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import kotlinx.android.synthetic.main.fragment_display_search_news.*
import kotlinx.android.synthetic.main.fragment_live_news.*

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
        setupListener()
        createProgressDialog()
        setupRecyclerView()
    }

    /**
     * Add click listeners to views
     */
    private fun setupListener() {
        searchNews_SV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNews_SV.clearFocus()
                // Fetch the News
                getDemoLiveNews()
                // Make news view visible and hide others
                updateUI()
                return false // Returning false will hide the keyboard
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    /**
     * Visible the view which shows the news
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
                NewsDataClass("This is dummy title of News. This is dummy title of News",
                    "This is a dummy new description for UI purpose. This is dummy title of News. This is dummy title of News",
                    "https://static.vecteezy.com/system/resources/previews/000/228/631/non_2x/vector-news-background-with-text-live-updates.jpg",
                    "24-04-2021")
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