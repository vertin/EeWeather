package com.vertin.eetweather.db.repository

import com.vertin.eetweather.db.PlaceForecastDAO
import com.vertin.eetweather.domain.model.PlaceForecast
import com.vertin.eetweather.domain.toPlaceForecast
import com.vertin.eetweather.domain.toPlaceForecastDTO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PlaceForecastStorageRoom @Inject constructor(private val dao: PlaceForecastDAO) :
    PlaceForecastStorage {
    override fun savePlacesForecast(list: List<PlaceForecast>): Completable {
        return dao.clearAll().andThen(dao.savePlacesForecast(list.map { it.toPlaceForecastDTO() }))
    }

    override fun loadPlacesForecast(): Single<List<PlaceForecast>> {
        return dao.loadPlacesForecast().map { it.map { it.toPlaceForecast() } }
    }
}