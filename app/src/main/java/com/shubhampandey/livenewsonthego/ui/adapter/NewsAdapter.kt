package com.shubhampandey.livenewsonthego.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shubhampandey.livenewsonthego.R
import com.shubhampandey.livenewsonthego.data.dataclass.NewsDataClass
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewsAdapter(private val context: Context, private val dataSet: List<NewsDataClass>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsTitle: TextView = view.findViewById(R.id.compactNewsTitle_TV)
        val newsPublishedAt: TextView =
            view.findViewById(R.id.compactNewsPublishedAt_TV)
        val newsImage: ImageView = view.findViewById(R.id.compactNewsImage_IV)
        val newsSource: TextView = view.findViewById(R.id.compactNewsSource_TV)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_compact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    // Replace the contents of a view (invoked by the layout manager)
    // Used to replace/update views at a specific position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindItems(holder, position)
    }

    private fun bindItems(holder: ViewHolder, position: Int) {
        holder.newsTitle.text = dataSet[position].newsTitle
        val originalDate = dataSet[position].newsPublishedAt.substring(0, 10)
        val formatter: DateTimeFormatter?
        val date: LocalDate?
        val formattedDate: String?
        // Date formatter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.parse(originalDate)
            formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
            formattedDate = date.format(formatter)
        } else {
            formattedDate = originalDate
        }
        holder.newsPublishedAt.text = formattedDate
        holder.newsPublishedAt.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.icon_date_range,
            0,
            0,
            0
        )
        holder.newsPublishedAt.compoundDrawablePadding = 8

        holder.newsSource.text = dataSet[position].newsSource
        holder.newsSource.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.icon_source,
            0,
            0,
            0
        )
        holder.newsSource.compoundDrawablePadding = 8

        val newsImgURL = dataSet[position].newsThumbnailImageURL
        Glide.with(context).load(newsImgURL).placeholder(R.drawable.live_news_image)
            .into(holder.newsImage)

        // Attach the click listener to each item
        holder.itemView.setOnClickListener {
            // Prepare data to send it to next destination
            val bundle = bundleOf(
                "newsID" to dataSet[position].id,
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