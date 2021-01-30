package com.vertin.eetweather.db.repository

import com.vertin.eetweather.domain.model.PlaceForecast
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface PlaceForecastStorage {
    fun savePlacesForecast(list: List<PlaceForecast>): Completable
    fun loadPlacesForecast(): Single<List<PlaceForecast>>
}