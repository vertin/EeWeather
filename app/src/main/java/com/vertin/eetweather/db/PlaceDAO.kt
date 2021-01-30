package com.vertin.eetweather.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface PlaceDAO {
    @Insert
    fun savePlaces(places: List<PlaceDTO>): Completable

    @Query("DELETE from PlaceDTO")
    fun clear(): Completable

    @Query("SELECT * FROM PlaceDTO WHERE name LIKE :query")
    fun getPlacesByName(query: String): Single<List<PlaceDTO>>

    @Query("SELECT * FROM PlaceDTO Where latitude=:latitude and longitude=:longitude LIMIT 1")
    fun getByLocation(latitude: Double, longitude: Double): Single<List<PlaceDTO>>

    @Query("SELECT * FROM PlaceDTO")
    fun getAll(): Single<List<PlaceDTO>>
}