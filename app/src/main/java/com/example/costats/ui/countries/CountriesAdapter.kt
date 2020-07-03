package com.example.costats.ui.countries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.costats.R
import com.example.costats.models.Country
import kotlinx.android.synthetic.main.layout_country_item.view.*

class CountriesAdapter(private val glide: RequestManager) :
    ListAdapter<Country, CountriesAdapter.ViewHolder>(CountryDC()) {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_country_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    fun swapData(data: List<Country>) = submitList(data.toMutableList())

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(country: Country) = with(itemView) {

            val url = "https://www.countryflags.io/${country.code}/flat/64.png"
            glide.load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_flag)

            txt_country_name.text = country.name

            setOnClickListener { listener?.onClick(country) }

        }
    }

    interface Listener {
        fun onClick(country: Country)
    }


    private class CountryDC : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(
            oldItem: Country,
            newItem: Country
        ): Boolean = oldItem.code == newItem.code

        override fun areContentsTheSame(
            oldItem: Country,
            newItem: Country
        ): Boolean = oldItem == newItem

    }
}
