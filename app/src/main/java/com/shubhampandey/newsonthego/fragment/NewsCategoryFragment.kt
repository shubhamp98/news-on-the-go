package com.shubhampandey.newsonthego.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shubhampandey.newsonthego.R
import kotlinx.android.synthetic.main.fragment_news_category.*

class NewsCategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        categoryGeneralNews.setOnClickListener {
            // TODO
            // Pass data using safe args, key="GENERAL_CATEGORY, value="general
        }
        categoryBusinessNews.setOnClickListener {

        }
        categoryEntNews.setOnClickListener {

        }
        categoryScienceNews.setOnClickListener {

        }
        categorySportsNews.setOnClickListener {

        }
        catHealthNews.setOnClickListener {

        }
        categoryTechNews.setOnClickListener {

        }
    }


}