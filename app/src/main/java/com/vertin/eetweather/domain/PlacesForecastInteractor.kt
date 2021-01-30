package com.vertin.eetweather.domain

import com.vertin.eetweather.db.repository.PlaceForecastStorage
import com.vertin.eetweather.domain.model.PlaceForecast
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PlacesForecastInteractor @Inject constructor(private val placeForecastStorage: PlaceForecastStorage) {

    fun loadPlacesForecast(): Single<List<PlaceForecast>> {
        return placeForecastStorage.loadPlacesForecast()
    }
}