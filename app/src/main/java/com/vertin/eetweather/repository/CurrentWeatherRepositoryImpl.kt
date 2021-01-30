package com.vertin.eetweather.repository

import android.util.Log
import com.vertin.eetweather.remote.apis.WeatherApi
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrentWeatherRepositoryImpl @Inject constructor(private val api: WeatherApi) :
    CurrentWeatherRepository {
    override fun getCurrentWeather() =  api.apiEstoniaCurrentGet()
}