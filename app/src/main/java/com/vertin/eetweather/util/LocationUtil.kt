package com.vertin.eetweather.util

import android.location.Location

class LocationUtil {
    private val distanceContainer = FloatArray(1)

    /**
     * Wrapper for Android Location distance checker
     */
    fun calculateDistance(
        lat1: Double,
        lng1: Double,
        lat2: Double,
        lng2: Double,
    ): Float {
        Location.distanceBetween(
            lat1,
            lng1,
            lat2,
            lng2,
            distanceContainer
        )
        val distance = distanceContainer[0]
        distanceContainer[0] = 0f
        return distance
    }
}