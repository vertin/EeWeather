package com.vertin.eetweather.db.model

import com.vertin.eetweather.domain.model.PlacePreview
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface PlaceStorage {
    fun savePlaces(places: List<PlacePreview>): Completable
    fun loadPlace(query: String): Single<List<PlacePreview>>
    fun loadAll():Single<List<PlacePreview>>
    fun loadByLocation(latitude: Double, longitude: Double): Single<PlacePreview>
}