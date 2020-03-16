package com.johnfneto.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButtonToggleGroup
import com.johnfneto.weatherapp.R
import com.johnfneto.weatherapp.database.WeatherLocationsViewModel
import com.johnfneto.weatherapp.databinding.FragmentWeatherScreenBinding
import com.johnfneto.weatherapp.utils.*
import com.johnfneto.weatherapp.utils.PermissionsManager.checkPermissions
import com.johnfneto.weatherapp.utils.PermissionsManager.isLocationEnabled
import com.johnfneto.weatherapp.utils.Utils.hideKeyboard
import com.johnfneto.weatherapp.utils.Utils.isInternetAvailable
import com.johnfneto.weatherapp.weather_api.WeatherViewModel
import com.squareup.picasso.Picasso
import java.util.*

class WeatherScreenFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {
    private val TAG = javaClass.simpleName

    private val args: WeatherScreenFragmentArgs by navArgs()
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherLocationsViewModel: WeatherLocationsViewModel
    private lateinit var binding: FragmentWeatherScreenBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
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

        weatherLocationsViewModel = ViewModelProvider(this).get(WeatherLocationsViewModel::class.java)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        setHasOptionsMenu(true)
        setupRecentLocationsObserver()
        setupWeatherApiObserver()
        loadLastWeatherLocation(args.locationName)
        setupSearchText()

        //binding.cardView!!.setCardBackgroundColor(resources.getColor(R.color.backgroundColor))

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
                        if (isInternetAvailable(requireContext())) {
                            binding.progressBar.visibility = VISIBLE
                            weatherViewModel.getWeather(binding.searchText.toString())
                        }
                        hideKeyboard(requireActivity())
                    }
                }
                false
            }

            setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (isInternetAvailable(requireContext())) {
                        binding.progressBar.visibility = VISIBLE
                        if (textView.text.toString().matches("[0-9]+".toRegex()) && textView.text.toString().length == 5) {
                            weatherViewModel.getWeatherByZipCode(textView.text.toString())
                        } else {
                            weatherViewModel.getWeather(textView.text.toString())
                        }
                    }

                    hideKeyboard(requireActivity())
                }
                false
            }

            setOnItemClickListener { _, _, position, _ ->
                if (isInternetAvailable(requireContext())) {
                    binding.progressBar.visibility = VISIBLE
                    if (position == 0) {
                        getLastLocation()
                    } else {
                        weatherLocationsViewModel.dropdownLocationsList.value?.let { dropdownList ->
                            weatherViewModel.getWeather(dropdownList[position])
                        }
                    }
                    hideKeyboard(requireActivity())
                }
            }
        }
    }

    private fun setupRecentLocationsObserver() {
        weatherLocationsViewModel.dropdownLocationsList.observe(viewLifecycleOwner, Observer { dropdownList ->
            dropdownAdapter = ArrayAdapter<String>(requireContext(), R.layout.drop_down_layout, dropdownList)
            binding.searchText.threshold = 1
            binding.searchText.setAdapter(dropdownAdapter)
        })
    }

    private fun setupWeatherApiObserver() {
            weatherViewModel.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
            binding.weatherData = weatherData
            binding.progressBar.visibility = GONE

            binding.imageView?.let {
                val imageUrl = String.format(getString(R.string.build_image_url), weatherData.weather[0].icon)
                Picasso.get().load(imageUrl).resize(200, 200).into(binding.imageView)
            }

            binding.updatedAt.text = Utils.formatDate(Calendar.getInstance().timeInMillis)
            weatherData.updated = Calendar.getInstance().timeInMillis
            weatherLocationsViewModel.saveLocation(weatherData)

        })

        weatherViewModel.errorStatus.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = GONE
            }
        })
    }

    private fun loadLastWeatherLocation(locationName: String?) {
        if (isInternetAvailable(requireContext())) {
            binding.progressBar.visibility = VISIBLE
            if (locationName == null) {
                if (weatherLocationsViewModel.dropdownLocationsList.value == null) {
                    weatherLocationsViewModel.dropdownLocationsList.observeOnce(viewLifecycleOwner) { dropdownList ->
                        if (dropdownList.size > 1) {
                            weatherViewModel.getWeather(dropdownList[1])
                        } else {
                            getLastLocation()
                        }
                    }
                } else {
                    if (weatherLocationsViewModel.dropdownLocationsList.value!!.size > 1) {
                        weatherViewModel.getWeather(weatherLocationsViewModel.dropdownLocationsList.value!![1])
                    } else {
                        getLastLocation()
                    }
                }
            } else {
                weatherViewModel.getWeather(locationName)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions(requireContext())) {
            if (isLocationEnabled(requireContext())) {
                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        weatherViewModel.getWeatherByCoord(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location please", Toast.LENGTH_LONG).show()
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

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        setupLocationCallback()
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation: Location = locationResult.lastLocation
                weatherViewModel.getWeatherByCoord(
                    lastLocation.latitude.toString(),
                    lastLocation.longitude.toString()
                )
                stopLocationCallback()
            }
        }
    }

    private fun stopLocationCallback() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.temperature_button, menu)


        val item: MenuItem = menu.findItem(R.id.temperatureButton)
        val layout = item.actionView as RelativeLayout

        val materialButtonToggleGroup = layout[0] as MaterialButtonToggleGroup
        if (Preferences.getTemperatureUnit(requireContext()) == TemperatureUnitsEnum.FAHRENHEIT.name) {
            materialButtonToggleGroup.check(R.id.fahrenheitButton)
        }
        else {
            materialButtonToggleGroup.check(R.id.celsiusButton)
        }


        materialButtonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.celsiusButton -> {
                        Preferences.setTemperatureCelsius(requireContext())
                    }
                    R.id.fahrenheitButton -> {
                        Preferences.setTemperatureFahrenheit(requireContext())
                    }
                }
                binding.invalidateAll()
            }
        }
    }
}