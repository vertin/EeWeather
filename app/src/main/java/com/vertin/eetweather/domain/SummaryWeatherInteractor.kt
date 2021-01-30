package com.vertin.eetweather.domain

import com.vertin.eetweather.db.model.PlaceStorage
import com.vertin.eetweather.db.repository.PlaceForecastStorage
import com.vertin.eetweather.db.repository.WeatherForecastStorage
import com.vertin.eetweather.domain.model.ForecastPreview
import com.vertin.eetweather.domain.model.PlacePreview
import com.vertin.eetweather.domain.model.Summary
import com.vertin.eetweather.remote.models.Forecast
import com.vertin.eetweather.repository.CurrentWeatherRepository
import com.vertin.eetweather.repository.WeatherForecastRepository
import com.vertin.eetweather.util.LocationUtil
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import javax.inject.Inject

class SummaryWeatherInteractor @Inject constructor(
    private val forecastRepo: WeatherForecastRepository,
    private val currentWeatherRepo: CurrentWeatherRepository,
    private val placesLocalRepo: PlaceStorage,
    private val placeForecastLocalRepo: PlaceForecastStorage,
    private val localForecastRepo: WeatherForecastStorage,
    private val locationUtil: LocationUtil
) {

    fun getForecastWithInLocation(lat: Double, lng: Double) =
        currentWeatherRepo.getCurrentWeather()
            //Map station items to PlacePreview items
            .map {
                it.observations.map { it.toPlacePreview() }
            }
            //Cache places list
            .flatMap {
                placesLocalRepo.savePlaces(it).toSingle { it }
            }
            //Find closest station to user position
            .map { findClosestLocation(lat, lng, it) }
            //Get Forecast information
            .zipWith(getForecastInfo(), { t1, t2 -> Summary(t1, t2) })
            //On network error try to load from cache
            .onErrorResumeNext { networkErr ->
                if (networkErr is IOException) loadFromCache(lat, lng).onErrorResumeNext {
                    Single.error(
                        networkErr
                    )
                } else Single.error(networkErr)
            }


    private fun loadFromCache(lat: Double, lng: Double): Single<Summary> {
        return placesLocalRepo
            .loadAll()
            .doOnSuccess { if (it.isEmpty()) throw NoSuchElementException() }
            .map { findClosestLocation(lat, lng, it) }
            .zipWith(
                localForecastRepo.loadForecast()
                    .doOnSuccess { if (it.isEmpty()) throw NoSuchElementException() },
                { t1, t2 -> Summary(t1, t2, false) }
            )
    }

    private fun getForecastInfo(): Single<List<ForecastPreview>> =
        forecastRepo.getWeatherForecast()
            //Cache places forecast
            .flatMap {
                //Cache places forecast info
                Single.just(it).zipWith(cachePlacesForecast(it[0]), { t1, _ -> t1 })
            }
            .map { it.map { it.toForecastPreview() } }
            //Cache forecast info
            .flatMap { forecastList ->
                localForecastRepo.storeForecast(forecastList)
                    .toSingle {
                        forecastList
                    }
            }

    private fun cachePlacesForecast(forecast: Forecast): Single<Unit> {
        return forecast.extractPlaceForecast()?.let {
            placeForecastLocalRepo.savePlacesForecast(it)
                .toSingle { Unit }
        } ?: Single.just(Unit)
    }

    private fun findClosestLocation(
        lat: Double, lng: Double,
        observations: List<PlacePreview>
    ): PlacePreview {
        var closestDistance: Float? = null
        var closestStation: PlacePreview = observations[0]
        observations.onEach { station ->


            val result = locationUtil.calculateDistance(
                lat,
                lng,
                station.latitude,
                station.longitude
            )

            if (closestDistance ?: let { result } >= result) {
                closestStation = station
                closestDistance = result
            }
        }
        return closestStation
    }
}