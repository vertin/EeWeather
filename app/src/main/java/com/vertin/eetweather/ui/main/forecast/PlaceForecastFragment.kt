package com.vertin.eetweather.ui.main.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vertin.eetweather.R
import com.vertin.eetweather.databinding.PlaceForecastFragmentBinding
import com.vertin.eetweather.databinding.PlaceForecastItemBinding
import com.vertin.eetweather.domain.model.PlaceForecast
import com.vertin.eetweather.util.PhenomenonUiInterpreter
import com.vertin.eetweather.util.UiResult
import com.vertin.eetweather.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceForecastFragment : Fragment(R.layout.place_forecast_fragment) {

    private val placesViewModel by viewModels<PlaceForecastViewModel>()
    private val binding by viewBinding(PlaceForecastFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.forecastListView.layoutManager = LinearLayoutManager(requireContext())

        placesViewModel.placeForecastData.observe(viewLifecycleOwner, {
            when (it) {
                is UiResult.Success -> {
                    binding.forecastListView.adapter = PlaceForecastAdapter(it.t)
                }
                is UiResult.Failed -> {
                    Toast.makeText(
                        requireContext(),
                        it.err.msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

    }

    private class PlaceForecastAdapter(private val items: List<PlaceForecast>) :
        RecyclerView.Adapter<PlaceForecastAdapter.PlaceViewHolder>() {
        class PlaceViewHolder(private val bindind: PlaceForecastItemBinding) :
            RecyclerView.ViewHolder(bindind.root) {
            fun bind(place: PlaceForecast) {
                (place.dayPhenomenon ?: place.nightPhenomenon)?.let {
                    bindind.phenomenonDescr.text = it.nameValue
                    bindind.phenomenonImage.visibility = View.VISIBLE
                    bindind.phenomenonImage.text =
                        PhenomenonUiInterpreter.getPhenomenonAwesomeTest(it)
                } ?: run {
                    bindind.phenomenonImage.visibility = View.INVISIBLE
                }
                bindind.placeName.text = place.name
                bindind.dayTemperature.text = bindind.root.context.getString(
                    R.string.temperature_day,
                    (place.dayMin ?: place.dayMax).toString()
                )
                bindind.nightTemperature.text = bindind.root.context.getString(
                    R.string.temperature_night,
                    (place.nightMin ?: place.nightMax).toString()
                )

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
            return PlaceViewHolder(
                PlaceForecastItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }
}