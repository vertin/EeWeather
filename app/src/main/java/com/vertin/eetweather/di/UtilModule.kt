package com.vertin.eetweather.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vertin.eetweather.remote.tools.TypesAdapterFactory
import com.vertin.eetweather.remote.tools.XNullableAdapterFactory
import com.vertin.eetweather.util.LocationUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UtilModule {
    @Provides
    fun provideLocationUtil() = LocationUtil()

    @Provides
    fun provideMoshi() = Moshi.Builder()
        .add(XNullableAdapterFactory())
        .add(KotlinJsonAdapterFactory())
        .add(TypesAdapterFactory())
        .build()
}