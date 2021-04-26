package com.shubhampandey.newsonthego.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.shubhampandey.newsonthego.R
import kotlinx.android.synthetic.main.fragment_display_full_news.*

class DisplayFullNewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_full_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()
    }

    private fun updateUI() {
        val newsTitle = arguments?.getString("newsTitle")
        val newsDescription = arguments?.getString("newsDescription")
        val newsImageURL = arguments?.getString("newsImgURL")
        val newsPublishedAt = arguments?.getString("newsPublishedAt")

        fullNewsTitle_TV.text = newsTitle
        fullNewsDesc_TV.text = newsDescription
        Glide.with(this).load(newsImageURL).placeholder(R.drawable.live_news_image)
            .into(fullNewsImg_IV)

    }
}