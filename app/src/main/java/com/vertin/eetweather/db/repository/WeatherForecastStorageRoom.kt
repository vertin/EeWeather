package com.vertin.eetweather.db.repository

import android.util.Log
import com.vertin.eetweather.db.ForecastDAO
import com.vertin.eetweather.domain.model.ForecastPreview
import com.vertin.eetweather.domain.toForecastDTO
import com.vertin.eetweather.domain.toForecastPreview
import com.vertin.eetweather.remote.models.Forecast
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherForecastStorageRoom @Inject constructor(private val forecastDAO: ForecastDAO) :
    WeatherForecastStorage {
    override fun storeForecast(forecastList: List<ForecastPreview>): Completable {
        return forecastDAO.clearAll()
            .andThen(
                forecastDAO.saveForecast(forecastList.map {
                    try {
                        it.toForecastDTO()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        throw e
                    }
                })
            )

    }

    override fun loadForecast(): Single<List<ForecastPreview>> {
        return forecastDAO.loadAll().map { it.map { it.toForecastPreview() } }
    }
}