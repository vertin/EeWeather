package com.vertin.eetweather.di

import com.squareup.moshi.Moshi
import com.vertin.eetweather.remote.apis.WeatherApi
import com.vertin.eetweather.remote.tools.GeneratedCodeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideWeatherApi(httpClient: OkHttpClient, moshi: Moshi) =
        Retrofit
            .Builder()
            .client(httpClient)
            .baseUrl("https://weather.aw.ee/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
            .addConverterFactory(GeneratedCodeConverters.converterFactory(moshi))
            .build()
            .create(WeatherApi::class.java)
}