package com.vertin.eetweather.domain

import com.vertin.eetweather.db.*
import com.vertin.eetweather.domain.model.*
import com.vertin.eetweather.remote.models.Day
import com.vertin.eetweather.remote.models.Forecast
import com.vertin.eetweather.remote.models.Station

fun Station.toPlacePreview(): PlacePreview {
    return PlacePreview(
        name,
        airtemperature,
        latitude!!.toDouble(),
        longitude!!.toDouble(),
        wind = Wind(winddirection, windspeed),
        findPhenomenonByName(phenomenon),
        relativehumidity,
        uvindex,
        airpressure
    )
}

fun PlacePreview.toPlaceDTO(): PlaceDTO {
    return PlaceDTO(
        name = name,
        lastKnownTemperature = temperature,
        latitude = latitude,
        longitude = longitude,
        lastKnownPhenomenon = phenomenon?.nameValue,
        windSpeed = wind?.speed,
        windDir = wind?.direction,
        humidity = relativehumidity,
        uvindex = uvindex,
        airpressure = airpressure
    )
}

fun PlaceDTO.toPlacePreview(): PlacePreview {
    return PlacePreview(
        name,
        lastKnownTemperature,
        latitude,
        longitude,
        Wind(windDir, windSpeed),
        findPhenomenonByName(lastKnownPhenomenon),
        humidity,
        uvindex,
        airpressure
    )
}

fun Forecast.toForecastPreview(): ForecastPreview {
    return ForecastPreview(date = date, day = day.toDayPreview(), night = night.toDayPreview())
}

fun ForecastPreview.toForecastDTO(): ForecastDTO {
    return ForecastDTO(date = date, day = day.toDayDTO(), night = night.toDayDTO())
}

fun ForecastDTO.toForecastPreview(): ForecastPreview {
    return ForecastPreview(date, day.toDayPreview(), night.toDayPreview())
}

fun Day.toDayPreview(): DayPreview {
    return DayPreview(
        phenomenon = findPhenomenonByName(phenomenon),
        tempMin = tempmin?.toInt(),
        tempMax = tempmax?.toInt(),
        peipsi = peipsi,
        seaTemp = sea
    )
}

fun DayDTO.toDayPreview(): DayPreview {
    return DayPreview(
        tempmin,
        tempmax,
        text,
        Wind(windDir, windSpeed),
        sea,
        findPhenomenonByName(phenomenon),
        peipsi
    )
}

fun DayPreview.toDayDTO(): DayDTO {
    return DayDTO(
        phenomenon = phenomenon?.nameValue,
        tempmin = tempMin,
        tempmax = tempMax,
        text = description,
        sea = seaTemp,
        peipsi = peipsi,
        windDir = wind?.direction,
        windSpeed = wind?.speed
    )
}

fun Forecast.extractPlaceForecast(): List<PlaceForecast>? {
    return this.day.places?.map { day ->
        val night = this.night.places?.find { it.name == day.name }
        PlaceForecast(
            day.name,
            day.tempmin,
            day.tempmax,
            nightMin = night?.tempmin,
            nightMax = night?.tempmax,
            dayPhenomenon = findPhenomenonByName(day.phenomenon),
            nightPhenomenon = findPhenomenonByName(night?.phenomenon)
        )
    }
}

fun PlaceForecast.toPlaceForecastDTO(): PlaceForecastDTO {
    return PlaceForecastDTO(
        name = name,
        dayMin = dayMin,
        dayMax = dayMax,
        nightMax = nightMax,
        nightMin = nightMin,
        dayPhenomenon = dayPhenomenon?.nameValue,
        nightPhenomenon = nightPhenomenon?.nameValue
    )
}

fun PlaceForecastDTO.toPlaceForecast(): PlaceForecast {
    return PlaceForecast(
        name,
        dayMin,
        dayMax,
        nightMin,
        nightMax,
        findPhenomenonByName(dayPhenomenon),
        findPhenomenonByName(nightPhenomenon)
    )
}

