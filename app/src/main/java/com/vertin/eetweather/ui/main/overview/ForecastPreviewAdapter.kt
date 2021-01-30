package com.vertin.eetweather.ui.main.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vertin.eetweather.R
import com.vertin.eetweather.databinding.ForecastPreviewItemBinding
import com.vertin.eetweather.domain.model.ForecastPreview
import com.vertin.eetweather.util.PhenomenonUiInterpreter

class ForecastPreviewAdapter(private val items: List<ForecastPreview>) :
    RecyclerView.Adapter<ForecastPreviewAdapter.ForecastHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        return ForecastHolder(
            ForecastPreviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ForecastHolder(private val itemBinding: ForecastPreviewItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(forecast: ForecastPreview) {
            (forecast.day.phenomenon ?: forecast.night.phenomenon)?.let {
                itemBinding.weatherIcon.text = PhenomenonUiInterpreter.getPhenomenonAwesomeTest(it)
                itemBinding.phenomenon.text = it.nameValue
            }
            itemBinding.day.text = forecast.date

            itemBinding.dayTemperature.text =
                itemBinding.root.context.getString(R.string.temperature_format, forecast.day.tempMin,forecast.day.tempMax)
            itemBinding.nightTemperature.text =
                itemBinding.root.context.getString(R.string.temperature_format, forecast.night.tempMin,forecast.night.tempMax)

        }
    }
}