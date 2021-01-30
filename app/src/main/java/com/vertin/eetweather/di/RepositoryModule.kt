package com.vertin.eetweather.di

import com.vertin.eetweather.db.model.PlaceStorage
import com.vertin.eetweather.db.repository.*
import com.vertin.eetweather.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@InstallIn(ActivityComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindForecastRepository(repository: WeatherForecastRepositoryImpl): WeatherForecastRepository

    @Binds
    abstract fun bindForecastStorageRepository(storage: WeatherForecastStorageRoom): WeatherForecastStorage

    @Binds
    abstract fun bindPlacesStorageRepository(storage: PlaceStorageRoom): PlaceStorage

    @Binds
    abstract fun bindPlacesForecastStorage(storage: PlaceForecastStorageRoom): PlaceForecastStorage

    @Binds
    abstract fun bindCurrentWeatherRepository(repositoryImpl: CurrentWeatherRepositoryImpl): CurrentWeatherRepository

    @Binds
    abstract fun bindPlacePreferencesRepository(appPrefs: AppPreferencesImpl): PlacePreferences

    @Binds
    abstract fun bindFeaturePreferencesRepository(appPrefs: AppPreferencesImpl): FeaturePreferences

}