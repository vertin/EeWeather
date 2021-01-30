package com.vertin.eetweather.db.repository

import com.vertin.eetweather.domain.model.ForecastPreview
import com.vertin.eetweather.remote.models.Forecast
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface WeatherForecastStorage {
    fun storeForecast(forecastList: List<ForecastPreview>): Completable
    fun loadForecast(): Single<List<ForecastPreview>>
}