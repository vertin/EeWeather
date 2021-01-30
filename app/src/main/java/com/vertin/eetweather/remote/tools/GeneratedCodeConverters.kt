package com.vertin.eetweather.remote.tools

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory

object GeneratedCodeConverters {

    @JvmStatic
    fun converterFactory(moshi: Moshi): Converter.Factory {
        return WrapperConverterFactory(
                CollectionFormatConverterFactory(),
                EnumToValueConverterFactory(),
                MoshiConverterFactory.create(moshi)
        )
    }
}
