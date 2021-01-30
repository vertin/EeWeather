package com.vertin.eetweather.db.repository

import com.vertin.eetweather.db.PlaceDAO
import com.vertin.eetweather.db.model.PlaceStorage
import com.vertin.eetweather.domain.model.PlacePreview
import com.vertin.eetweather.domain.toPlaceDTO
import com.vertin.eetweather.domain.toPlacePreview
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Supplier
import javax.inject.Inject

class PlaceStorageRoom @Inject constructor(private val dao: PlaceDAO) : PlaceStorage {
    override fun savePlaces(places: List<PlacePreview>) =
        dao.clear()
            .andThen(dao.savePlaces(places.map { it.toPlaceDTO() }))



    override fun loadPlace(query: String): Single<List<PlacePreview>> {
        return dao.getPlacesByName("%$query%").map {
            it.map {
                it.toPlacePreview()
            }
        }
    }

    override fun loadAll(): Single<List<PlacePreview>> =
        dao.getAll().map { it.map { it.toPlacePreview() } }

    override fun loadByLocation(latitude: Double, longitude: Double): Single<PlacePreview> {
        return dao.getByLocation(latitude, longitude)
            .map {
                if (it.isEmpty()) {
                    throw NoSuchElementException()
                } else {
                    it.first().toPlacePreview()
                }
            }
    }


}