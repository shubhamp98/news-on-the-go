package com.shubhampandey.newsonthego.ui.fragment

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.data.dataclass.NewsDataClass
import com.shubhampandey.newsonthego.ui.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_display_full_news.*

class DisplayFullNewsFragment : Fragment() {

    private var newsId: Int? = null
    private var newsTitle: String? = null
    private var newsDescription: String? = null
    private var newsImageURL: String? = null
    private var newsPublishedAt: String? = null
    private var newsSource: String? = null
    private var newsURL: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_full_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        setListener()
        updateUI()
    }

    /**
     * Retrieve data from bundle which is passed to this destination
     */
    private fun getData() {
        newsId = arguments?.getInt("newsID")
        newsTitle = arguments?.getString("newsTitle")
        newsDescription = arguments?.getString("newsDescription")
        newsImageURL = arguments?.getString("newsImgURL")
        newsPublishedAt = arguments?.getString("newsPublishedAt")
        newsSource = arguments?.getString("newsSource")
        newsURL = arguments?.getString("url")
    }


    private fun setListener() {
        addToBookMarkOutLine_IB.setOnClickListener {
            // If news Id is 0 then data is coming
            // from Network as API is not returning newsId
            // and we using Default value from dataclass
            if (newsId == 0) {
                addToBookMarkOutLine_IB.setImageResource(R.drawable.ic_baseline_star_40)
                saveNewsInDB()
            }
            // Data is from localDB, we have rowID/newsID
            else {
                removeNewsFromDB(newsId)
            }
        }

        shareNews_IB.setOnClickListener {
            shareNews()
        }

    }

    private fun removeNewsFromDB(newsId: Int?) {
        val application: Application = requireActivity().application
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel(application)::class.java)
        if (newsId != 0) {
            val news = NewsDataClass(
                newsId,
                newsTitle.toString(),
                newsDescription.toString(),
                newsImageURL.toString(),
                newsPublishedAt.toString(),
                newsSource.toString(),
                newsURL.toString()
            )
            newsViewModel.deleteNewsFromDB(news)
            findNavController().navigateUp()
            showNewsRemoveSnackbar()
        }
    }

    /**
     * Creating intent to share the news
     */
    private fun shareNews() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            // (Optional) Here we're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "Get News On The Go!")
            putExtra(
                Intent.EXTRA_TEXT,
                "Hey,\n\nLook at this news.\n\nNews source: $newsURL"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    /**
     * Save news item to local database
     */
    private fun saveNewsInDB() {
        val application: Application = requireActivity().application
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel(application)::class.java)
        newsViewModel.saveNewsToDB(
            NewsDataClass(
                null,
                newsTitle.toString(),
                newsDescription.toString(),
                newsImageURL.toString(),
                newsPublishedAt.toString(),
                newsSource.toString(),
                newsURL.toString()
            )
        )
        showNewsSavedSnackbar()
    }

    private fun showNewsSavedSnackbar() {
        Snackbar.make(
            requireContext(),
            requireView(),
            "News bookmarked successfully!",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showNewsRemoveSnackbar() {
        Snackbar.make(
            requireContext(),
            requireView(),
            "News removed successfully!",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * Update the UI with news details
     */
    private fun updateUI() {
        fullNewsTitle_TV.text = newsTitle
        fullNewsDesc_TV.text = newsDescription
        Glide.with(this).load(newsImageURL).placeholder(R.drawable.live_news_image)
            .into(fullNewsImg_IV)
        // If news Id is 0 then data is coming
        // from Network as API is not returning newsId
        // and we using Default value from dataclass
        if (newsId == 0) {
            addToBookMarkOutLine_IB.setImageResource(R.drawable.ic_baseline_star_border_40)
        }
        // Data is from localDB, we have rowID/newsID
        else {
            addToBookMarkOutLine_IB.setImageResource(R.drawable.ic_baseline_star_40)
        }
    }
}