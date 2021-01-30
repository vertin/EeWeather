package com.vertin.eetweather.repository

import com.vertin.eetweather.remote.models.ObservationsModel
import io.reactivex.rxjava3.core.Single

interface CurrentWeatherRepository {
    fun getCurrentWeather(): Single<ObservationsModel>
}