package com.shubhampandey.newsonthego.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.ui.adapter.NewsAdapter
import com.shubhampandey.newsonthego.data.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.utils.SharedPrefUtil
import com.shubhampandey.newsonthego.ui.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_display_category_news.*

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
        displayCategoryType_TV.text = categoryType.toUpperCase()
        when (categoryType) {
            getString(R.string.news_category_general) ->  {
                displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_blur_circular,
                    0,
                    0,
                    0
                )
                displayCategoryType_TV.compoundDrawablePadding = 8
            }

            getString(R.string.news_category_business) -> {
                displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_business,
                    0,
                    0,
                    0
                )
                displayCategoryType_TV.compoundDrawablePadding = 8
            }
            getString(R.string.news_category_entertainment) -> {
                displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_movie,
                    0,
                    0,
                    0
                )
                displayCategoryType_TV.compoundDrawablePadding = 8
            }
            getString(R.string.news_category_health) -> {
                displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_health_and_safety,
                    0,
                    0,
                    0
                )
                displayCategoryType_TV.compoundDrawablePadding = 8
            }
            getString(R.string.news_category_science) -> {
                displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_science,
                    0,
                    0,
                    0
                )
                displayCategoryType_TV.compoundDrawablePadding = 8
            }
            getString(R.string.news_category_sports) -> {
                displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_sports_cricket,
                    0,
                    0,
                    0
                )
                displayCategoryType_TV.compoundDrawablePadding = 8
            }
            getString(R.string.news_category_technology) -> {
                displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_computer,
                    0,
                    0,
                    0
                )
                displayCategoryType_TV.compoundDrawablePadding = 8
            }
            // By default show General news icon
            else -> {
                displayCategoryType_TV.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.icon_blur_circular,
                    0,
                    0,
                    0
                )
                displayCategoryType_TV.compoundDrawablePadding = 8
            }
        }
    }

    /**
     * Function to generate dummy news to avoid
     * API quota limit exceeding
     */
    private fun getDemoLiveNews() {
        showAnimatedLoader()
        for (i in 0..10) {
            newsDataset.add(
                NewsDataClass(
                    null,
                    "This is dummy title of News. This is dummy title of News",
                    "This is a dummy new description for UI purpose. This is dummy title of News. This is dummy title of News",
                    "https://static.vecteezy.com/system/resources/previews/000/228/631/non_2x/vector-news-background-with-text-live-updates.jpg",
                    "2021-04-24",
                    "Dummy Source",
                    "hello"
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
            category = categoryType,
            searchKeyword = null
        )
        newsViewModel.newsResponsesLiveData.observe(viewLifecycleOwner, Observer {
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
                //Log.e(TAG, getString(R.string.error_message))
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
            getString(R.string.error_message),
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