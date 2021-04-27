package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.database.RoomDatabaseBuilder
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import kotlinx.android.synthetic.main.fragment_bookmarked_news.*
import kotlinx.android.synthetic.main.fragment_display_category_news.*
import java.util.concurrent.Executors

class BookmarkedNewsFragment : Fragment() {

    private val TAG = BookmarkedNewsFragment::class.java.simpleName
    lateinit var progressDialog: ProgressDialog
    private var newsDataset = ArrayList<NewsDataClass>()
    private lateinit var customNewsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarked_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        getLiveNews()
    }

    private fun getLiveNews() {
        val roomDatabaseBuilder = context?.let { RoomDatabaseBuilder.getInstance(it) }
        Executors.newSingleThreadExecutor().execute {
            // get data from Database
            val newsList = roomDatabaseBuilder?.newsDao()?.getAllNews()!!
            bookmarkedNewsList_RV.apply {
                // Stuff that updates the UI
                activity?.runOnUiThread {
                    customNewsAdapter =
                        NewsAdapter(
                            context,
                            newsList as ArrayList<NewsDataClass>
                        )
                    bookmarkedNewsList_RV.adapter = customNewsAdapter
                    customNewsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupUI() {
        createProgressDialog()
        setupRecyclerView()
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
        bookmarkedNewsList_RV.layoutManager = linearLayoutManager
        customNewsAdapter = context?.let { NewsAdapter(it, newsDataset) }!!
        // attach adapter
        bookmarkedNewsList_RV.adapter = customNewsAdapter
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