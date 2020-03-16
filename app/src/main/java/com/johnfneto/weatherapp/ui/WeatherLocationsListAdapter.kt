package com.johnfneto.weatherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.johnfneto.weatherapp.R
import com.johnfneto.weatherapp.databinding.LocationsListItemBinding
import com.johnfneto.weatherapp.models.WeatherModel

class WeatherLocationsListAdapter(
    private val onClickListener: View.OnClickListener,
    private val onLongClickListener: View.OnLongClickListener
) : RecyclerView.Adapter<WeatherLocationsListAdapter.DataBindingViewHolder>() {
    private val TAG = javaClass.simpleName

    private lateinit var binding: LocationsListItemBinding
    private var locationsList = emptyList<WeatherModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.locations_list_item, parent, false
        )
        return DataBindingViewHolder(binding)
    }

    override fun getItemCount() = locationsList.size

    internal fun updateLocations(locationsList: List<WeatherModel>) {
        this.locationsList = locationsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        holder.bind(locationsList[position])
    }

    inner class DataBindingViewHolder(private val binding: LocationsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherModel) {
            binding.root.tag = item
            binding.weatherData = item

            itemView.setOnClickListener(onClickListener)
            itemView.setOnLongClickListener(onLongClickListener)
        }
    }
}