package com.vertin.eetweather.di

import com.vertin.eetweather.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    fun provideWeatherDao(db: WeatherDatabase) = db.forecastDao()

    @Provides
    fun providePlaceDao(db: WeatherDatabase) = db.placeDao()

    @Provides
    fun providePlaceForecastDao(db: WeatherDatabase) = db.placeForecastDao()
}