package com.vertin.eetweather.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FeaturePreferences {
    companion object {
        const val CURRENT_LOCATION_FEATURE = "CURRENT_LOCATION_FEATURE"
    }
    fun enableFeature(feature:String, enable:Boolean): Completable
    fun isFeatureEnabled(feature:String): Single<Boolean>
}