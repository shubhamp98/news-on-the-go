package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.network.ApiClient
import kotlinx.android.synthetic.main.fragment_live_news.*
import retrofit2.Callback
import retrofit2.Response

class LiveNewsFragment : Fragment() {

    private val TAG = LiveNewsFragment::class.java.simpleName
    lateinit var progressDialog: ProgressDialog
    var newsDataset = ArrayList<NewsDataClass>()
    private lateinit var customNewsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    /**
     * Call functions to setup the UI
     */
    private fun setupUI() {
        createProgressDialog()
        setupRecyclerView()
        //getLiveNews()
        getDemoLiveNews()
        setClickListeners()
    }

    private fun setClickListeners() {
        searchNews_FAB.setOnClickListener {
            navigateToDisplaySearchedNewsDestination()
        }
    }

    private fun navigateToDisplaySearchedNewsDestination() {
        // Navigate to a destination
        val action = LiveNewsFragmentDirections.actionLiveNewsFragmentToDisplaySearchedNewsFragment()
        findNavController().navigate(action)
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
        val call = ApiClient.getClient.getNews(getString(R.string.mediastacknews_access_key), getString(
                    R.string.default_country_india), null, getString(R.string.default_language_english))
        call.enqueue(object : Callback<ResponseDataClass> {
            override fun onFailure(call: retrofit2.Call<ResponseDataClass>, t: Throwable) {
                //Log.d(TAG, "Error is ${t.message}")
                Toast.makeText(context, getString(R.string.live_news_fetching_error_msg), Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                response.body()?.newsData?.let { newsDataset.addAll(it) }
                compactNewsList_RV.adapter!!.notifyDataSetChanged()
                dismissProgressDialog()
                //Log.d(TAG, "Data is ${response.body()}")
            }

        })
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