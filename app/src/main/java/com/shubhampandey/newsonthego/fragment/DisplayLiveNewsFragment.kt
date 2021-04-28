package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_display_live_news.*
import kotlinx.android.synthetic.main.fragment_display_search_news.*
import retrofit2.Callback
import retrofit2.Response

class DisplayLiveNewsFragment : Fragment() {

    private val TAG = DisplayLiveNewsFragment::class.java.simpleName
    lateinit var progressDialog: ProgressDialog
    var newsDataset = ArrayList<NewsDataClass>()
    private lateinit var customNewsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_live_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun onResume() {
        super.onResume()
//        getLiveNews()
        getDemoLiveNews()
    }

    /**
     * Call functions to setup the UI
     */
    private fun setupUI() {
        createProgressDialog()
        setupRecyclerView()
        setClickListeners()
    }

    private fun setClickListeners() {
        searchNews_FAB.setOnClickListener {
            navigateToDisplaySearchedNewsDestination()
        }
    }

    private fun navigateToDisplaySearchedNewsDestination() {
        // Navigate to a destination
        val action =
            DisplayLiveNewsFragmentDirections.actionLiveNewsFragmentToDisplaySearchedNewsFragment()
        findNavController().navigate(action)
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
        compactNewsList_RV.adapter!!.notifyDataSetChanged()
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
        compactNewsList_RV.layoutManager = linearLayoutManager
        customNewsAdapter = context?.let { NewsAdapter(it, newsDataset) }!!
        // attach adapter
        compactNewsList_RV.adapter = customNewsAdapter
    }

    /**
     * Fetch live news by calling the news API
     */
    private fun getLiveNews() {
        showProgressDialog()
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.getNewsFromAPI(
            accessKey = getString(R.string.mediastacknews_access_key),
            category = null,
            country = getString(R.string.default_country_india),
            searchKeyword = null,
            fetchLimit = null,
            language = getString(R.string.default_language_english),
            sort = getString(R.string.default_sort_order
            ))
            newsViewModel.newsResponsesLiveData.observe(viewLifecycleOwner, Observer {
                //Log.i(TAG, "Data is ${it.newsData}")
                if (it != null) {
                    newsDataset.clear()
                    newsDataset.addAll(it.newsData)
                    compactNewsList_RV.adapter!!.notifyDataSetChanged()
                }
                else {
                    // Handle your error here
                    Log.i(TAG, "No Data Found")
                }
                dismissProgressDialog()

        })
    }

    /**
     * Show the progress dialog
     */
    private fun showProgressDialog() {
        progressDialog.show()
    }

    /**
     * Hide the progress dialog
     */
    private fun dismissProgressDialog() {
        progressDialog.dismiss()
    }
}