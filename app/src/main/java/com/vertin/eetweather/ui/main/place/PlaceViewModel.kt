package com.vertin.eetweather.ui.main.place

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vertin.eetweather.domain.PlaceInteractor
import com.vertin.eetweather.domain.model.InternalFail
import com.vertin.eetweather.domain.model.PlacePreview
import com.vertin.eetweather.util.UiResult
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class PlaceViewModel @ViewModelInject constructor(private val interactor: PlaceInteractor) :
    ViewModel() {


    private val disposable: CompositeDisposable = CompositeDisposable()

    fun findPlace(query: CharSequence) {
        disposable.add(interactor.loadPlaces(query.toString()).subscribeOn(Schedulers.io())
            .doOnSubscribe { filteredPlaces.postValue(UiResult.Loading()) }
            .subscribeBy(
                onSuccess = { filteredPlaces.postValue(UiResult.Success(it)) },
                onError = {
                    filteredPlaces.postValue(
                        UiResult.Failed(
                            InternalFail("Request error")
                        )
                    )
                })
        )
    }

    val filteredPlaces = MutableLiveData<UiResult<List<PlacePreview>>>()

    val assignSucceed = MutableLiveData<UiResult<Boolean>>()


    fun assignPlace(place: PlacePreview) {
        disposable.add(interactor.assignPlace(place).subscribeOn(Schedulers.io())
            .doOnSubscribe { assignSucceed.postValue(UiResult.Loading()) }
            .subscribeBy(
                onComplete = { assignSucceed.postValue(UiResult.Success(true)) },
                onError = { assignSucceed.postValue(UiResult.Failed(InternalFail("Can't save selected place"))) }
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}