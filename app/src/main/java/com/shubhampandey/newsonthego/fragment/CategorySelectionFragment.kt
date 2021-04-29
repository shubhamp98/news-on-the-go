package com.shubhampandey.newsonthego.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shubhampandey.newsonthego.MyApplication
import com.shubhampandey.newsonthego.R
import kotlinx.android.synthetic.main.fragment_news_category.*

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

        if(checkForNetwork())
            setListeners()
        else {
            categorySelection_LL.visibility = View.INVISIBLE
            categorySelection_LAV.visibility = View.VISIBLE
        }
    }

    /**
     * Check for Internet connection
     */
    private fun checkForNetwork() =
        MyApplication().hasNetwork()

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