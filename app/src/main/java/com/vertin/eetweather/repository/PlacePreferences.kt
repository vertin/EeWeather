package com.vertin.eetweather.repository

import com.vertin.eetweather.domain.model.PlacePreview
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface PlacePreferences {
    fun resetAssignedPlace():Completable
    fun assignToPlace(place: PlacePreview): Completable
    fun getAssignedPlace(): Single<PlacePreview>

}