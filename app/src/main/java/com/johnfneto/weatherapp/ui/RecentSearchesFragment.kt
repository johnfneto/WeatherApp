package com.johnfneto.weatherapp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.johnfneto.weatherapp.R
import com.johnfneto.weatherapp.database.WeatherLocationsViewModel
import com.johnfneto.weatherapp.models.WeatherLocation
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_recent_searches.*


class RecentSearchesFragment : Fragment(R.layout.fragment_recent_searches) {
    private val TAG = javaClass.simpleName

    private lateinit var weatherLocationsViewModel: WeatherLocationsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherLocationsViewModel = ViewModelProvider(this).get(WeatherLocationsViewModel::class.java)

        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        recycleListView.layoutManager = llm
        val adapter = WeatherLocationsListAdapter(
            onItemClickListener,
            onLongClickListener)
        recycleListView.adapter = adapter

        weatherLocationsViewModel.locationsList.observe(viewLifecycleOwner, Observer { locationsList ->
            locationsList?.let {
                adapter.updateLocations(it)
            }
        })
    }

    private val onItemClickListener =
        View.OnClickListener { view ->
            val location = view.tag as WeatherLocation

            val args = Bundle()
            args.putString("locationName", location.city)
            Navigation.findNavController(getView()!!).navigate(R.id.weatherScreenFragment, args)

            // We should be using the code below, but there is a bug on the API:
            // Too many arguments for @NonNull public open fun actionGotoWeatherScreen()
            //val action = RecentSearchesFragmentDirections.actionGotoWeatherScreen(locationName)
            //findNavController().navigate(action)
        }

    private val onLongClickListener =
        View.OnLongClickListener { view ->
            val location = view.tag as WeatherLocation

            deleteLocation(location)
            false
        }

    private fun deleteLocation(location: WeatherLocation) {
        val factory = LayoutInflater.from(requireContext())
        val dialogView = factory.inflate(R.layout.dialog_layout, null)

        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.setView(dialogView)
        dialogView.findViewById<View>(R.id.okButton).setOnClickListener {
            weatherLocationsViewModel.deleteLocation(location.uid!!)
            dialog.dismiss()
        }
        dialogView.findViewById<View>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }
        val msgText: TextView = dialogView.findViewById(R.id.message)
        msgText.text = String.format(resources.getString(R.string.delete_location), location.city)
        dialog.show()
    }
}