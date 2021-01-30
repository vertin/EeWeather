package com.vertin.eetweather.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ForecastDTO::class, PlaceDTO::class, PlaceForecastDTO::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun forecastDao(): ForecastDAO
    abstract fun placeDao(): PlaceDAO
    abstract fun placeForecastDao(): PlaceForecastDAO

}