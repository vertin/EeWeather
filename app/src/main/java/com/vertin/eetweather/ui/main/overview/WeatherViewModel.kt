package com.vertin.eetweather.ui.main.overview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.vertin.eetweather.domain.LocationException
import com.vertin.eetweather.domain.LocationInteractor
import com.vertin.eetweather.domain.SummaryWeatherInteractor
import com.vertin.eetweather.domain.model.InternalFail
import com.vertin.eetweather.domain.model.Summary
import com.vertin.eetweather.repository.FeaturePreferences
import com.vertin.eetweather.repository.PlacePreferences
import com.vertin.eetweather.util.NoInternetConnection
import com.vertin.eetweather.util.UiResult
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException

class WeatherViewModel @ViewModelInject constructor(
    private val forecastInteractor: SummaryWeatherInteractor,
    private val locationInteractor: LocationInteractor,
    private val placePreferences: PlacePreferences,
    private val featurePreferences: FeaturePreferences
) :
    ViewModel() {

    companion object {
        private val DEFAULT_LOCATION = LatLng(58.766263, 25.919722)
    }

    private val disposable: CompositeDisposable = CompositeDisposable()

    val forecast = MutableLiveData<UiResult<Summary>>()

    val myLocationFeature = MutableLiveData<Boolean>()

    private var skipCurrentLocation = true

    init {
        disposable.add(
            featurePreferences.isFeatureEnabled(FeaturePreferences.CURRENT_LOCATION_FEATURE)
                .subscribeOn(Schedulers.io())
                .subscribeBy(onSuccess = {
                    myLocationFeature.postValue(it)
                })
        )
    }


    fun getForecast() {
        disposable.add(
            userLocation()
                .flatMap { forecastInteractor.getForecastWithInLocation(it.latitude, it.longitude) }
                .doOnSubscribe { forecast.postValue(UiResult.Loading()) }
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = {
                        forecast.postValue(UiResult.Success(it))
                    },
                    onError = {
                        when (it) {
                            is LocationException -> {
                                forecast.postValue(UiResult.Failed(it.locationFail))
                            }
                            is IOException -> {
                                forecast.postValue(UiResult.Failed(NoInternetConnection()))
                            }
                            else -> {
                                forecast.postValue(UiResult.Failed(InternalFail("Server Error")))
                            }
                        }
                    }
                )
        )
    }

    private fun userLocation(): Single<LatLng> =

        placePreferences.getAssignedPlace()
            .flatMap {
                Single.just(LatLng(it.latitude, it.longitude))
            }
            .onErrorResumeNext {
                if (it is NoSuchElementException) {
                    if (skipCurrentLocation) {
                        Single.just(DEFAULT_LOCATION)
                    } else
                        featurePreferences.isFeatureEnabled(FeaturePreferences.CURRENT_LOCATION_FEATURE)
                            .flatMap {
                                if (it) {
                                    locationInteractor.getCurrentLocation()
                                } else {
                                    Single.just(DEFAULT_LOCATION)
                                }
                            }
                } else {
                    Single.error(it)
                }
            }


    fun getForecastUndefinedLocation() {
        skipCurrentLocation = true
        getForecast()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()

    }

    /**
     * Disable the Current Location feature
     */
    fun disableLocationFeature() {
        myLocationFeature.postValue(false)
        disposable.add(
            featurePreferences
                .enableFeature(FeaturePreferences.CURRENT_LOCATION_FEATURE, false)
                .subscribeOn(Schedulers.io()).subscribe()
        )
    }

    fun checkOnMyLocation() {
        skipCurrentLocation = false
        disposable.add(
            placePreferences.resetAssignedPlace().subscribeOn(Schedulers.io()).subscribe {
                getForecast()
            })
    }

}