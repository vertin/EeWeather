package com.vertin.eetweather.util

import com.vertin.eetweather.domain.model.Phenomenon

object PhenomenonUiInterpreter {
    val phenomenonMap = mapOf(
        Pair(Phenomenon.CLEAR, "sun"),
        Pair(Phenomenon.HEAVY_RAIN, "cloud-showers-heavy"),
        Pair(Phenomenon.LIGHT_RAIN, "cloud-showers-heavy"),
        Pair(Phenomenon.MODERATE_RAIN, "cloud-showers-heavy"),
        Pair(Phenomenon.CLOUDY, "cloud"),
        Pair(Phenomenon.CLOUDY_WITH_CLEAR_SPELLS, "cloud"),
        Pair(Phenomenon.FEW_CLOUDS, "cloud-sun"),
        Pair(Phenomenon.VARIABLE_CLOUDS, "cloud-sun"),
        Pair(Phenomenon.FOG, "fog"),
        Pair(Phenomenon.HAIL, "cloud-meatball"),
        Pair(Phenomenon.MIST, "fog"),
        Pair(Phenomenon.SNOWSTORM, "snowflakes"),
        Pair(Phenomenon.DRIFTING_SNOW, "snowflake"),
        Pair(Phenomenon.HEAVY_SNOWFALL, "snowflake"),
        Pair(Phenomenon.HEAVY_SNOW_SHOWER, "snowflake"),
        Pair(Phenomenon.MODERATE_SNOWFALL, "snowflake"),
        Pair(Phenomenon.LIGHT_SNOWFALL, "snowflake"),
        Pair(Phenomenon.LIGHT_SNOWFALL, "snowflake"),
        Pair(Phenomenon.LIGHT_SHOWER, "cloud-showers-heavy"),
        Pair(Phenomenon.LIGHT_SNOW_SHOWER, "cloud-showers-heavy"),
        Pair(Phenomenon.MODERATE_SHOWER, "cloud-showers-heavy"),
        Pair(Phenomenon.HEAVY_SHOWER, "cloud-showers-heavy"),
        Pair(Phenomenon.RISK_OF_GLAZE, "snowflake"),
        Pair(Phenomenon.LIGHT_SLEET, "snowflake"),
        Pair(Phenomenon.MODERATE_SLEET, "snowflake"),
        Pair(Phenomenon.THUNDER, "bolt"),
        Pair(Phenomenon.THUNDERSTORM, "bolt")
    )

    fun getPhenomenonAwesomeTest(phenomenon: Phenomenon): String? {
        return phenomenonMap.get(phenomenon)
    }
}