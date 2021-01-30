package com.vertin.eetweather.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface ForecastDAO {
    @Query("DELETE FROM ForecastDTO")
    fun clearAll(): Completable

    @Insert
    fun saveForecast(list: List<ForecastDTO>):Completable

    @Query("SELECT * FROM ForecastDTO")
    fun loadAll(): Single<List<ForecastDTO>>
}