package com.vertin.eetweather.domain

import com.vertin.eetweather.db.model.PlaceStorage
import com.vertin.eetweather.domain.model.PlacePreview
import com.vertin.eetweather.repository.PlacePreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PlaceInteractor @Inject constructor(
    private val placesRepository: PlaceStorage,
    private val appPreferences: PlacePreferences
) {

    fun loadPlaces(query: String): Single<List<PlacePreview>> = placesRepository.loadPlace(query)

    fun assignPlace(place: PlacePreview): Completable = appPreferences.assignToPlace(place)

}