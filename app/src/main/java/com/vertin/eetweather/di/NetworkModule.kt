package com.vertin.eetweather.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideHttpClient() = OkHttpClient
        .Builder()
        .build()
}