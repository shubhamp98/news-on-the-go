package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.database.RoomDatabaseBuilder
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.viewmodel.NewsViewModel
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
    }

    override fun onResume() {
        super.onResume()
        getSavedNews()
    }

    private fun getSavedNews() {
        showProgressDialog()
        val application = requireActivity().application
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel(application)::class.java)
        newsViewModel.getNewsFromDB()
        newsViewModel.bookmarkedNewsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                //Log.i(TAG, "Data is $it")
                newsDataset.clear()
                newsDataset.addAll(it)
                bookmarkedNewsList_RV.adapter!!.notifyDataSetChanged()
            }
            dismissProgressDialog()
        })

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