package com.johnfneto.weatherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.johnfneto.weatherapp.R
import com.johnfneto.weatherapp.models.WeatherLocation
import com.johnfneto.weatherapp.databinding.LocationsListItemBinding

class WeatherLocationsListAdapter(
    private val onClickListener: View.OnClickListener,
    private val onLongClickListener: View.OnLongClickListener
) : RecyclerView.Adapter<WeatherLocationsListAdapter.DataBindingViewHolder>() {
    private val TAG = javaClass.simpleName

    private lateinit var binding: LocationsListItemBinding
    private var locationsList = emptyList<WeatherLocation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.locations_list_item, parent, false
        )
        return DataBindingViewHolder(binding)
    }

    override fun getItemCount() = locationsList.size

    internal fun updateLocations(locationsList: List<WeatherLocation>) {
        this.locationsList = locationsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        holder.bind(locationsList[position])
    }

    inner class DataBindingViewHolder(private val binding: LocationsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherLocation) {
            binding.root.tag = item
            binding.location = item

            itemView.setOnClickListener(onClickListener)
            itemView.setOnLongClickListener(onLongClickListener)
        }
    }
}