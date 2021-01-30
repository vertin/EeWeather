package com.vertin.eetweather.di

import android.content.Context
import androidx.room.Room
import com.vertin.eetweather.db.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {
    @Provides
    fun provideDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()
}