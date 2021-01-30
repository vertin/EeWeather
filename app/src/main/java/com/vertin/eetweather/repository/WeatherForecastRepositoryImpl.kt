package com.vertin.eetweather.repository

import com.vertin.eetweather.remote.apis.WeatherApi
import com.vertin.eetweather.remote.models.Forecast
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherForecastRepositoryImpl @Inject constructor(private val api: WeatherApi) :
    WeatherForecastRepository {
    override fun getWeatherForecast(): Single<List<Forecast>> =
        api.apiEstoniaForecastGet().map { it.forecasts }
}