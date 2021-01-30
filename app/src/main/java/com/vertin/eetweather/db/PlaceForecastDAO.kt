package com.vertin.eetweather.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface PlaceForecastDAO {
    @Query("DELETE FROM PlaceForecastDTO")
    fun clearAll():Completable

    @Insert
    fun savePlacesForecast(forecast: List<PlaceForecastDTO>): Completable

    @Query("SELECT * FROM PlaceForecastDTO")
    fun loadPlacesForecast(): Single<List<PlaceForecastDTO>>
}