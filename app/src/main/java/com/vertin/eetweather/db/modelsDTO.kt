package com.vertin.eetweather.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.vertin.eetweather.domain.model.Wind


@Entity
data class ForecastDTO(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") val uid: Long? = null,
    val date: String,
    @Embedded(prefix = "day_")
    val day: DayDTO,
    @Embedded(prefix = "night_")
    val night: DayDTO
)

@Entity
data class PlaceForecastDTO(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") val uid: Long? = null,
    val name: String,
    val dayMin: Double? = null,
    val nightMin: Double? = null,
    val dayMax: Double? = null,
    val nightMax: Double? = null,
    val dayPhenomenon: String? = null,
    val nightPhenomenon: String? = null
)


data class DayDTO(
    val phenomenon: String? = null,
    val tempmin: Int? = null,
    val tempmax: Int? = null,
    val text: String? = null,
    val sea: String? = null,
    val peipsi: String? = null,
    val windDir: String? = null,
    val windSpeed: String? = null
)

@Entity
data class PlaceDTO(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") val uid: Long? = null,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val lastKnownTemperature: String? = null,
    val lastKnownPhenomenon: String? = null,
    val windSpeed: String? = null,
    val windDir: String? = null,
    val humidity: String? = null,
    val uvindex: String? = null,
    val airpressure: String? = null
)