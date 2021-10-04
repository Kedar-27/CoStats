package com.example.costats.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.costats.R
import com.example.costats.models.Country
import kotlinx.android.synthetic.main.layout_stats_item.view.*

class StatsAdapter : ListAdapter<Country, StatsAdapter.ViewHolder>(CountryDC()) {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_stats_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    fun swapData(data: List<Country>) = submitList(data.toMutableList())

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(country: Country) = with(itemView) {

            txt_name.text = country.name
            txt_total_deaths.text = "${country.totalDeaths}"
            txt_total_cases.text = "${country.totalConfirmed}"
            txt_total_recoveries.text = "${country.totalRecovered}"

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
