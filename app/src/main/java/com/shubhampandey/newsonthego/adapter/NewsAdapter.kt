package com.shubhampandey.newsonthego.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shubhampandey.newsonthego.R
import com.shubhampandey.newsonthego.dataclass.NewsDataClass

class NewsAdapter(private val context: Context, private val dataSet: List<NewsDataClass>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val compactNewsTitleTextView: TextView = view.findViewById(R.id.compactNewsTitle_TV)
        val compactNewsPublishedAtTextView: TextView = view.findViewById(R.id.compactNewsPublishedAt_TV)
        val compactNewsImageView: ImageView = view.findViewById(R.id.compactNewsImage_IV)
        val compactNewsSourceTextView: TextView = view.findViewById(R.id.compactNewsSource_TV)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_compact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    // Replace the contents of a view (invoked by the layout manager)
    // Used to replace/update views at a specific position
    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.compactNewsTitleTextView.text = dataSet[position].newsTitle
        holder.compactNewsPublishedAtTextView.text = " " + dataSet[position].newsPublishedAt
        holder.compactNewsPublishedAtTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_date_range_20, 0, 0, 0)
        holder.compactNewsSourceTextView.text = " " + dataSet[position].newsSource
        holder.compactNewsSourceTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_source_20, 0, 0, 0)

        val newsImgURL = dataSet[position].newsThumbnailImageURL
        Glide.with(context).load(newsImgURL).placeholder(R.drawable.live_news_image)
            .into(holder.compactNewsImageView)

        // Attach the click listener to each item
        holder.itemView.setOnClickListener {
            // Prepare data to send it to next destination
            val bundle = bundleOf(
                "newsTitle" to dataSet[position].newsTitle,
                "newsDescription" to dataSet[position].newsDescription,
                "newsImgURL" to dataSet[position].newsThumbnailImageURL,
                "newsPublishedAt" to dataSet[position].newsPublishedAt,
                "newsSource" to dataSet[position].newsSource,
                "url" to dataSet[position].newsURL
            )
            // Navigate to next data
            it.findNavController().navigate(R.id.displayFullNewsFragment, bundle)
        }
    }
}