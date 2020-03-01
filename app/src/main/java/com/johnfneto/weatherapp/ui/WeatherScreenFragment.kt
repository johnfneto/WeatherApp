package com.johnfneto.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.johnfneto.weatherapp.R
import com.johnfneto.weatherapp.database.LocationsViewModel
import com.johnfneto.weatherapp.databinding.FragmentWeatherScreenBinding
import com.johnfneto.weatherapp.utils.PERMISSION_ID
import com.johnfneto.weatherapp.utils.PermissionsManager.checkPermissions
import com.johnfneto.weatherapp.utils.PermissionsManager.isLocationEnabled
import com.johnfneto.weatherapp.utils.Utils
import com.johnfneto.weatherapp.utils.Utils.hideKeyboard
import com.johnfneto.weatherapp.utils.observeOnce
import com.johnfneto.weatherapp.weather_api.WeatherRepository
import com.johnfneto.weatherapp.weather_api.WeatherViewModel
import com.squareup.picasso.Picasso
import java.util.*


class WeatherScreenFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private val args: WeatherScreenFragmentArgs by navArgs()
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var locationsViewModel: LocationsViewModel
    private lateinit var binding: FragmentWeatherScreenBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var dropdownAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weather_screen, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationsViewModel = ViewModelProvider(this).get(LocationsViewModel::class.java)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        setupRecentLocationsObserver()
        setupWeatherApiObserver()
        loadLastLocation(args.locationName)
        setupSearchText()

        binding.recentSearchesButton.setOnClickListener {
            val action = WeatherScreenFragmentDirections.actionGotoRecentSearches()
            findNavController().navigate(action)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearchText() {

        binding.searchText.apply {

            setOnTouchListener { _: View, event: MotionEvent ->
                if (binding.searchText.text.toString().isNotEmpty()) {
                    dropdownAdapter.filter.filter(null)
                }
                binding.searchText.showDropDown()

                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (binding.searchText.right - binding.searchText.compoundDrawables[2].bounds.width())) {
                        weatherViewModel.getWeather(binding.searchText.toString())
                        hideKeyboard(requireActivity())
                    }
                }
                false
            }

            setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.text.toString().matches("[0-9]+".toRegex()) && textView.text.toString().length == 5) {
                        weatherViewModel.getWeatherByZipCode(textView.text.toString())
                    } else {
                        weatherViewModel.getWeather(textView.text.toString())
                    }
                    hideKeyboard(requireActivity())
                }
                false
            }

            setOnItemClickListener { _, _, position, _ ->
                if (position == 0) {
                    getLastLocation()
                } else {
                    locationsViewModel.dropdownLocationsList.value?.let { dropdownList ->
                        weatherViewModel.getWeather(dropdownList[position])
                    }
                }
                hideKeyboard(requireActivity())
            }
        }
    }

    private fun setupRecentLocationsObserver() {
        locationsViewModel.dropdownLocationsList.observe(viewLifecycleOwner, Observer {  dropdownList ->
            dropdownAdapter = ArrayAdapter<String>(requireContext(), R.layout.drop_down_layout, dropdownList)
            binding.searchText.threshold = 1
            binding.searchText.setAdapter(dropdownAdapter)
        })
    }

    private fun setupWeatherApiObserver() {
        WeatherRepository.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
            WeatherRepository.weatherData.value?.let {
                binding.weatherData = weatherData

                binding.windDirection?.let {
                    it.rotation = 90F
                }

                val imageUrl = String.format(getString(R.string.build_image_url), weatherData.weather[0].icon)
                Picasso.get().load(imageUrl).resize(200, 200).into(binding.imageView)

                binding.updatedAt.text = Utils.formatDate(Calendar.getInstance().timeInMillis)
                locationsViewModel.saveLocation(String.format(resources.getString(R.string.city), weatherData.name, weatherData.sys.country))

                Log.d(
                    TAG,
                    " wind -> (${weatherData.wind.deg}) ${Utils.formatBearing(weatherData.wind.deg)}"
                )

            }
        })

        WeatherRepository.errorStatus.observe(viewLifecycleOwner, Observer { errorStatus ->
            Toast.makeText(requireContext(), resources.getString(R.string.unknown_location), Toast.LENGTH_SHORT).show()
        })
    }

    private fun loadLastLocation(locationName: String?) {
        if (locationName == null) {
            if (locationsViewModel.dropdownLocationsList.value == null) {
                locationsViewModel.dropdownLocationsList.observeOnce(viewLifecycleOwner) { dropdownList ->
                    if (dropdownList.size > 1) {
                        weatherViewModel.getWeather(dropdownList[1])
                    }
                    else {
                        getLastLocation()
                    }
                }
            }
            else {
                if (locationsViewModel.dropdownLocationsList.value!!.size > 1) {
                    weatherViewModel.getWeather(locationsViewModel.dropdownLocationsList.value!![1])
                }
                else {
                    getLastLocation()
                }
            }
        }
        else {
            weatherViewModel.getWeather(locationName)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions(requireContext())) {
            if (isLocationEnabled(requireContext())) {

                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        weatherViewModel.getWeatherByCoord(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_ID
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}