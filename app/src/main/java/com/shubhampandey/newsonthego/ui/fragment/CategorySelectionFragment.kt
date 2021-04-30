package com.shubhampandey.newsonthego.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shubhampandey.newsonthego.MyApplication
import com.shubhampandey.newsonthego.R
import kotlinx.android.synthetic.main.fragment_news_category.*
import kotlinx.android.synthetic.main.no_internet.*

class CategorySelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (hasNetworkConnectivity()) {
            // User has internet connectivity
            tryConnectivity()
        } else {
            // Internet connectivity failed
            categorySelection_LL.visibility = View.GONE
            showInternetConnectivityError()
            category_frag_no_connection_Layout.visibility = View.VISIBLE
            noConnectionRetry_Btn.setOnClickListener {
                tryConnectivity()
            }
        }
    }

    /**
     * Retry connectivity and update the UI
     */
    private fun tryConnectivity() {
        if (hasNetworkConnectivity()) {
            categorySelection_LL.visibility = View.VISIBLE
            setListeners()
            category_frag_no_connection_Layout.visibility = View.GONE
        } else {
            categorySelection_LL.visibility = View.GONE
            showInternetConnectivityError()
        }
    }

    /**
     * Check for Internet connectivity
     */
    private fun hasNetworkConnectivity() =
        MyApplication().hasNetwork()

    private fun showInternetConnectivityError() {
        Snackbar.make(requireView(), getString(R.string.internet_connectivity_error), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Attach listeners to category views
     */
    private fun setListeners() {
        categoryGeneralNews.setOnClickListener {
            navigateToDisplayCategoryNewsDestination(
                getString(R.string.news_category_general)
            )
        }
        categoryBusinessNews.setOnClickListener {
            navigateToDisplayCategoryNewsDestination(
                getString(R.string.news_category_business)
            )
        }
        categoryEntNews.setOnClickListener {
            navigateToDisplayCategoryNewsDestination(getString(R.string.news_category_entertainment))
        }
        categoryScienceNews.setOnClickListener {
            navigateToDisplayCategoryNewsDestination(getString(R.string.news_category_science))
        }
        categorySportsNews.setOnClickListener {
            navigateToDisplayCategoryNewsDestination(getString(R.string.news_category_sports))
        }
        categoryHealthNews.setOnClickListener {
            navigateToDisplayCategoryNewsDestination(getString(R.string.news_category_health))
        }
        categoryTechNews.setOnClickListener {
            navigateToDisplayCategoryNewsDestination(getString(R.string.news_category_technology))
        }
    }

    private fun navigateToDisplayCategoryNewsDestination(categoryType: String) {
        val action =
            CategorySelectionFragmentDirections.actionNewsCategoryFragmentToDisplaySingleCategoryNewsFragment(
                categoryType
            )
        findNavController().navigate(action)
    }


}