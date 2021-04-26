package com.shubhampandey.newsonthego.fragment

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.adapter.NewsAdapter
import com.shubhampandey.newsonthego.dataclass.NewsDataClass
import kotlinx.android.synthetic.main.fragment_display_category_news.*
import kotlinx.android.synthetic.main.fragment_display_search_news.*

class DisplayCategoryNewsFragment : Fragment() {

    val args: DisplayCategoryNewsFragmentArgs by navArgs()
    private val TAG = DisplayCategoryNewsFragment::class.java.simpleName
    lateinit var progressDialog: ProgressDialog
    var newsDataset = ArrayList<NewsDataClass>()
    private lateinit var customNewsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_category_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        getDemoLiveNews()
    }

    private fun setupUI() {
        createProgressDialog()
        setupRecyclerView()

        // Update the UI for selected the category
        val categoryType = args.categoryType
        displayCategoryType_TV.text = "Category: ${categoryType.toUpperCase()}"
    }

    private fun getDemoLiveNews() {
        showProgressDialog()
        for (i in 0..10) {
            newsDataset.add(
                NewsDataClass(
                    "This is dummy title of News. This is dummy title of News",
                    "This is a dummy new description for UI purpose. This is dummy title of News. This is dummy title of News",
                    "https://static.vecteezy.com/system/resources/previews/000/228/631/non_2x/vector-news-background-with-text-live-updates.jpg",
                    "24-04-2021",
                    "Dummy Source"
                )
            )
        }
        displayCategoryNews_RV.adapter!!.notifyDataSetChanged()
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
        displayCategoryNews_RV.layoutManager = linearLayoutManager
        customNewsAdapter = context?.let { NewsAdapter(it, newsDataset) }!!
        // attach adapter
        displayCategoryNews_RV.adapter = customNewsAdapter
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