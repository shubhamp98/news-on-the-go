package com.shubhampandey.newsonthego.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    // Replace the contents of a view (invoked by the layout manager)
    // Used to replace/update views at a specific position
    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.compactNewsTitleTextView.text = dataSet[position].newsTitle
        holder.compactNewsPublishedAtTextView.text = context.getString(R.string.published_at_placeholder) + " " + dataSet[position].newsPublishedAt
        val newsImgURL = dataSet[position].newsThumbnailImageURL
        Glide.with(context).load(newsImgURL).placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.compactNewsImageView)

    }
}