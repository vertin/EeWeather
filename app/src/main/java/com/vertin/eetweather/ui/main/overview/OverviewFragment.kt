package com.vertin.eetweather.ui.main.overview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vertin.eetweather.R
import com.vertin.eetweather.databinding.WeatherFragmentBinding
import com.vertin.eetweather.domain.model.*
import com.vertin.eetweather.util.NoInternetConnection
import com.vertin.eetweather.util.UiResult
import com.vertin.eetweather.util.ViewAnimation
import com.vertin.eetweather.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.weather_fragment) {

    companion object {
        const val LOCATION_PERMISSION_REQUEST = 123
        fun newInstance() = OverviewFragment()
    }


    private val binding by viewBinding(WeatherFragmentBinding::bind)


    private val viewModel by viewModels<WeatherViewModel>()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getForecast()
        binding.selectPlaceView.setOnClickListener { findNavController().navigate(R.id.action_overviewFragment_to_selectPlaceFragment) }
        viewModel.myLocationFeature.observe(viewLifecycleOwner, {
            if (it) {
                binding.myLocation.visibility = View.VISIBLE
            } else {
                binding.myLocation.visibility = View.GONE
            }
        })
        viewModel.forecast.observe(viewLifecycleOwner, { result ->
            when (result) {
                is UiResult.Loading -> {
                    ViewAnimation.fadeIn(binding.loadingView)
                }
                is UiResult.Failed -> {
                    ViewAnimation.fadeOut(binding.loadingView)
                    when (result.err) {
                        is LocationFail.PermissionRequired -> {
                            requestLocationPermission()
                        }
                        is LocationFail.Failed -> {
                            viewModel.getForecastUndefinedLocation()
                        }
                        is NoInternetConnection -> {
                            binding.noInternetConnection.noInternetConnectionView.visibility =
                                View.VISIBLE
                        }
                    }
                }
                is UiResult.Success -> {
                    ViewAnimation.fadeOut(binding.loadingView)
                    bindData(result.t)

                }
            }

        })

        binding.noInternetConnection.retry.setOnClickListener {
            binding.noInternetConnection.noInternetConnectionView.visibility =
                View.GONE
            viewModel.getForecast()
        }

        binding.myLocation.setOnClickListener {
            viewModel.checkOnMyLocation()
        }
        binding.more.setOnClickListener {
            findNavController().navigate(R.id.action_overviewFragment_to_placeForecastFragment)
        }
    }

    private fun bindData(t: Summary) {
        bindCurrentDay(t.placeWeather)
        bindNextForecast(t.forecast)
    }

    private fun bindNextForecast(subList: List<ForecastPreview>) {
        binding.forecastListView.layoutManager = LinearLayoutManager(requireContext())
        binding.forecastListView.adapter = ForecastPreviewAdapter(subList)
    }

    private fun bindCurrentDay(today: PlacePreview) {
        binding.currentTemp.text = getString(R.string.temperature, today.temperature)
        binding.placeName.text = today.name
        today.wind?.speed?.let {
            binding.currentWind.visibility = View.VISIBLE
            binding.currentWind.text = getString(R.string.prompt_wind_speed, it)
        } ?: run {
            binding.currentWind.visibility = View.GONE
        }
        today.wind?.direction?.let {
            binding.windDirection.visibility = View.VISIBLE
            binding.windDirection.rotation = it.toFloat()

        } ?: run {
            binding.windDirection.visibility = View.GONE
        }

        binding.phenomenon.text = today.phenomenon?.nameValue
    }


    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getForecast()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        viewModel.disableLocationFeature()
                    }
                }
                viewModel.getForecastUndefinedLocation()
            }
        }
    }

}