package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.dataclass.ResponseDataClass
import com.shubhampandey.newsonthego.network.ApiClient
import com.shubhampandey.newsonthego.util.SharedPrefUtil
import com.shubhampandey.newsonthego.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_display_category_news.*
import kotlinx.android.synthetic.main.fragment_display_live_news.*
import retrofit2.Callback
import retrofit2.Response

class DisplayCategoryNewsFragment : Fragment() {

    private val args: DisplayCategoryNewsFragmentArgs by navArgs()
    private val TAG = DisplayCategoryNewsFragment::class.java.simpleName
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
                    "Hello"
                )
            )
        }
        displayCategoryNews_RV.adapter!!.notifyDataSetChanged()
        hideAnimatedLoader()
    }

    /**
     * Fetch news category wise by calling the news API
     */
    private fun getCategoryWiseNews(categoryType: String) {
        showAnimatedLoader()
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.getNewsFromAPI(
            accessKey = getString(R.string.mediastacknews_access_key),
            category = categoryType,
            country = SharedPrefUtil.getCountryFromPref(requireActivity())!!,
            searchKeyword = null,
            fetchLimit = null,
            language = SharedPrefUtil.getLanguageFromPref(requireActivity())!!,
            sort = getString(R.string.default_sort_order
            ))
        newsViewModel.newsResponsesLiveData.observe(viewLifecycleOwner, Observer {
            //Log.i(TAG, "Data is ${it.newsData}")
            if (it != null) {
                newsDataset.clear()
                newsDataset.addAll(it.newsData)
                displayCategoryNews_RV.adapter!!.notifyDataSetChanged()
            }
            else {
                // Handle your error here
                Log.i(TAG, "No Data Found")
            }
            hideAnimatedLoader()

            //Log.i(TAG, "Data is ${it.newsData}")
            if (it != null) {
                if (it.newsData.isNotEmpty()) {
                    newsDataset.clear()
                    newsDataset.addAll(it.newsData)
                    displayCategoryNews_RV.adapter!!.notifyDataSetChanged()
                    display_category_no_info_layout.visibility = View.GONE
                    showList()
                } else {
                    // Data received is empty
                    hideList()
                    display_category_no_info_layout.visibility = View.VISIBLE
                }
            } else {
                // Handle your errors here
                Log.i(TAG, "Something went wrong!")
                hideList()
                display_category_no_info_layout.visibility = View.VISIBLE
                showError()
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
     * Make recyclerview visible
     */
    private fun showList() {
        displayCategoryNews_RV.visibility = View.VISIBLE
    }

    /**
     * Make recyclerview invisible
     */
    private fun hideList() {
        displayCategoryNews_RV.visibility = View.INVISIBLE
    }

    private fun showAnimatedLoader() {
        display_category_news_loading.visibility = View.VISIBLE
    }

    private fun hideAnimatedLoader() {
        display_category_news_loading.visibility = View.GONE
    }
}