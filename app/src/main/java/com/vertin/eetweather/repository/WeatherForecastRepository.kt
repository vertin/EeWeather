package com.vertin.eetweather.repository

import com.vertin.eetweather.remote.models.Forecast
import io.reactivex.rxjava3.core.Single

interface WeatherForecastRepository {
    fun getWeatherForecast(): Single<List<Forecast>>
}