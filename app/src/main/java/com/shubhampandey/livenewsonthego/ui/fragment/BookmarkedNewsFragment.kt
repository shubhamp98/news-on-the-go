package com.shubhampandey.livenewsonthego.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubhampandey.livenewsonthego.R
import com.shubhampandey.livenewsonthego.ui.adapter.NewsAdapter
import com.shubhampandey.livenewsonthego.data.dataclass.NewsDataClass
import com.shubhampandey.livenewsonthego.ui.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_bookmarked_news.*

class BookmarkedNewsFragment : Fragment() {

    private val TAG = BookmarkedNewsFragment::class.java.simpleName
    private var newsDataset = ArrayList<NewsDataClass>()
    private lateinit var customNewsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarked_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun getSavedNews() {
        showAnimatedLoader()
        val application = requireActivity().application
        val newsViewModel = ViewModelProvider(this).get(NewsViewModel(application)::class.java)
        newsViewModel.getNewsFromDB()
        newsViewModel.bookmarkedNewsLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                // There is bookmarked news found
                //Log.i(TAG, "Data is $it")
                newsDataset.clear()
                newsDataset.addAll(it)
                bookmarkedNewsList_RV.adapter!!.notifyDataSetChanged()
            } else {
                // no bookmarked news found
                //Log.i(TAG, "No bookmarked news found...")
                bookmark_frag_no_info_layout.visibility = View.VISIBLE
                bookmarkedNewsList_RV.visibility = View.GONE
            }
            hideAnimatedLoader()
        })

    }

    private fun setupUI() {
        setupRecyclerView()
        getSavedNews()
    }

    /**
     * Setup the recycler view and attach adapter to it
     */
    private fun setupRecyclerView() {
        // Set layout for RecyclerView
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        bookmarkedNewsList_RV.layoutManager = linearLayoutManager
        customNewsAdapter = context?.let { NewsAdapter(it, newsDataset) }!!
        // attach adapter
        bookmarkedNewsList_RV.adapter = customNewsAdapter
    }

    private fun showAnimatedLoader() {
        bookmark_frag_loading_layout.visibility = View.VISIBLE
    }

    private fun hideAnimatedLoader() {
        bookmark_frag_loading_layout.visibility = View.GONE
    }
}