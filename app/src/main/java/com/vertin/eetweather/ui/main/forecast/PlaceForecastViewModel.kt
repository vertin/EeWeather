package com.vertin.eetweather.ui.main.forecast

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vertin.eetweather.domain.PlacesForecastInteractor
import com.vertin.eetweather.domain.model.InternalFail
import com.vertin.eetweather.domain.model.PlaceForecast
import com.vertin.eetweather.util.UiResult
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class PlaceForecastViewModel @ViewModelInject constructor(private val interactor: PlacesForecastInteractor) :
    ViewModel() {


    private val disposable: CompositeDisposable = CompositeDisposable()

    val placeForecastData = MutableLiveData<UiResult<List<PlaceForecast>>>()

    init {
        loadPlacesForecast()
    }

    fun loadPlacesForecast() {
        disposable.add(
            interactor.loadPlacesForecast()
                .doOnSubscribe { placeForecastData.postValue(UiResult.Loading()) }
                .subscribeOn(Schedulers.io()).subscribeBy(onSuccess = {
                    placeForecastData.postValue(UiResult.Success(it))
                }, onError = {
                    placeForecastData.postValue(UiResult.Failed(InternalFail("No forecast found")))

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}
