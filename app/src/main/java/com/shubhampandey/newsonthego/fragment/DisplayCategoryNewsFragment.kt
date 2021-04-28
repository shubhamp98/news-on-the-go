package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.network.ApiClient
import com.shubhampandey.newsonthego.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_display_category_news.*
import kotlinx.android.synthetic.main.fragment_display_live_news.*
import retrofit2.Callback
import retrofit2.Response

class DisplayCategoryNewsFragment : Fragment() {

    private val args: DisplayCategoryNewsFragmentArgs by navArgs()
    private val TAG = DisplayCategoryNewsFragment::class.java.simpleName
    lateinit var progressDialog: ProgressDialog
    private var newsDataset = ArrayList<NewsDataClass>()
    private lateinit var customNewsAdapter: NewsAdapter
    private lateinit var categoryType: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_category_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveCategoryType()
        setupUI()
        //getDemoLiveNews()
    }

    override fun onResume() {
        super.onResume()
        getCategoryWiseNews(categoryType)
    }

    /**
     * Retrieve category type sent using Safe args
     */
    private fun retrieveCategoryType() {
        categoryType = args.categoryType
    }

    private fun setupUI() {
        createProgressDialog()
        setupRecyclerView()
        showSelectedCategoryInUI()
    }

    /**
     * Update the UI for selected the category
     */
    private fun showSelectedCategoryInUI() {
        displayCategoryType_TV.text = " " + categoryType.toUpperCase()
        when (categoryType) {
            getString(R.string.news_category_general) -> displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_blur_circular_24,
                0,
                0,
                0
            )
            getString(R.string.news_category_business) -> displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_business_center_24,
                0,
                0,
                0
            )
            getString(R.string.news_category_entertainment) -> displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_movie_24,
                0,
                0,
                0
            )
            getString(R.string.news_category_health) -> displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_health_and_safety_24,
                0,
                0,
                0
            )
            getString(R.string.news_category_science) -> displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_science_24,
                0,
                0,
                0
            )
            getString(R.string.news_category_sports) -> displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_sports_cricket_24,
                0,
                0,
                0
            )
            getString(R.string.news_category_technology) -> displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_computer_24,
                0,
                0,
                0
            )
            else -> displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_baseline_blur_circular_24,
                0,
                0,
                0
            )
        }
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
                    "Hello"
                )
            )
        }
        displayCategoryNews_RV.adapter!!.notifyDataSetChanged()
        dismissProgressDialog()
    }

    /**
     * Fetch news category wise by calling the news API
     */
    private fun getCategoryWiseNews(categoryType: String) {
        showProgressDialog()
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.getNewsFromAPI(
            accessKey = getString(R.string.mediastacknews_access_key),
            category = categoryType,
            country = getString(R.string.default_country_india),
            searchKeyword = null,
            fetchLimit = null,
            language = getString(R.string.default_language_english),
            sort = getString(R.string.default_sort_order
            ))
        newsViewModel.newsResponsesLiveData.observe(viewLifecycleOwner, Observer {
            //Log.i(TAG, "Data is ${it.newsData}")
            it?.let {
                newsDataset.addAll(it.newsData)
            }
            displayCategoryNews_RV.adapter!!.notifyDataSetChanged()
            dismissProgressDialog()

        })
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
        displayCategoryNews_RV.layoutManager = linearLayoutManager
        customNewsAdapter = context?.let { NewsAdapter(it, newsDataset) }!!
        // attach adapter
        displayCategoryNews_RV.adapter = customNewsAdapter
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