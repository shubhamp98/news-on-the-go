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
import com.google.android.material.snackbar.Snackbar
import com.shubhampandey.newsonthego.MyApplication
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_display_live_news.*
import kotlinx.android.synthetic.main.fragment_display_search_news.*
import kotlinx.android.synthetic.main.no_internet.*
import retrofit2.Callback
import retrofit2.Response

class DisplayLiveNewsFragment : Fragment() {

    private val TAG = DisplayLiveNewsFragment::class.java.simpleName
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

        if (hasNetworkConnectivity()) {
            tryConnectivity()
        } else {
            showInternetConnectivityError()
            hideList()
            no_connection_Layout.visibility = View.VISIBLE
            searchNews_FAB.visibility = View.INVISIBLE
            noConnectionRetry_Btn.setOnClickListener {
                tryConnectivity()
            }
        }
    }

    private fun tryConnectivity() {
        if (hasNetworkConnectivity()) {
            setupUI()
            getLiveNews()
//            getDemoLiveNews()
            no_connection_Layout.visibility = View.GONE
            searchNews_FAB.visibility = View.VISIBLE
        } else {
            showInternetConnectivityError()
        }
    }

    private fun showInternetConnectivityError() {
        Snackbar.make(requireView(), "Internet connectivity failed", Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Call functions to setup the UI
     */
    private fun setupUI() {
        setupRecyclerView()
        setClickListeners()
    }

    /**
     * Check for Internet connection
     */
    private fun hasNetworkConnectivity() =
        MyApplication().hasNetwork()

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
        showAnimatedLoader()
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
        hideAnimatedLoader()
        showList()
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
        showAnimatedLoader()
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.getNewsFromAPI(
            accessKey = getString(R.string.mediastacknews_access_key),
            category = null,
            country = getString(R.string.default_country_india),
            searchKeyword = null,
            fetchLimit = null,
            language = getString(R.string.default_language_english),
            sort = getString(
                R.string.default_sort_order
            )
        )
        newsViewModel.newsResponsesLiveData.observe(viewLifecycleOwner, Observer {
            //Log.i(TAG, "Data is ${it.newsData}")
            if (it != null) {
                if (it.newsData.isNotEmpty()) {
                    no_info_layout.visibility = View.GONE
                    newsDataset.clear()
                    newsDataset.addAll(it.newsData)
                    compactNewsList_RV.adapter!!.notifyDataSetChanged()
                    showList()
                } else {
                    // Data received is empty
                    hideList()
                    no_info_layout.visibility = View.VISIBLE
                }
            } else {
                // Handle your errors here
                hideList()
                showError()
                no_info_layout.visibility = View.VISIBLE
                searchNews_FAB.visibility = View.INVISIBLE
            }
            hideAnimatedLoader()
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
     * Make recyclerview visible
     */
    private fun showList() {
        compactNewsList_RV.visibility = View.VISIBLE
    }

    /**
     * Make recyclerview invisible
     */
    private fun hideList() {
        compactNewsList_RV.visibility = View.INVISIBLE
    }

    private fun showAnimatedLoader() {
        loading_layout.visibility = View.VISIBLE
    }

    private fun hideAnimatedLoader() {
        loading_layout.visibility = View.GONE
    }
}