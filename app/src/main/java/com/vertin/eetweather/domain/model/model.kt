package com.vertin.eetweather.domain.model


data class Summary(val placeWeather: PlacePreview, val forecast: List<ForecastPreview>,val online:Boolean=true)


data class PlacePreview(
    val name: String,
    val temperature: String?,
    val latitude: Double,
    val longitude: Double,
    val wind: Wind?,
    val phenomenon: Phenomenon?,
    val relativehumidity: String?,
    val uvindex: String?,
    val airpressure: String?
)

data class ForecastPreview(
    val date: String,
    val day: DayPreview,
    val night: DayPreview
)

data class PlaceForecast(
    val name: String,
    val dayMin: Double? = null,
    val dayMax: Double? = null,
    val nightMin: Double? = null,
    val nightMax: Double? = null,
    val dayPhenomenon: Phenomenon? = null,
    val nightPhenomenon: Phenomenon? = null
)

data class DayPreview(
    val tempMin: Int? = null,
    val tempMax: Int? = null,
    val description: String? = null,
    val wind: Wind? = null,
    val seaTemp: String? = null,
    val phenomenon: Phenomenon?=null,
    val peipsi: String?
)

data class Wind(val direction: String?, val speed: String?)

enum class Phenomenon(val nameValue: String) {
    CLEAR("Clear"),
    FEW_CLOUDS("Few clouds"),
    VARIABLE_CLOUDS("Variable clouds"),
    CLOUDY_WITH_CLEAR_SPELLS("Cloudy with clear spells"),
    CLOUDY("Cloudy"),
    LIGHT_SNOW_SHOWER("Light snow shower"),
    MODERATE_SNOW_SHOWER("Moderate snow shower"),
    HEAVY_SNOW_SHOWER("Heavy snow shower"),
    LIGHT_SHOWER("Light shower"),
    MODERATE_SHOWER("Moderate shower"),
    HEAVY_SHOWER("Heavy shower"),
    LIGHT_RAIN("Light rain"),
    MODERATE_RAIN("Moderate rain"),
    HEAVY_RAIN("Heavy rain"),
    RISK_OF_GLAZE("Risk of glaze"),
    LIGHT_SLEET("Light sleet"),
    MODERATE_SLEET("Moderate sleet"),
    LIGHT_SNOWFALL("Light snowfall"),
    MODERATE_SNOWFALL("Moderate snowfall"),
    HEAVY_SNOWFALL("Heavy snowfall"),
    SNOWSTORM("Snowstorm"),
    DRIFTING_SNOW("Drifting snow"),
    HAIL("Hail"),
    MIST("Mist"),
    FOG("Fog"),
    THUNDER("Thunder"),
    THUNDERSTORM("Thunderstorm")
}

fun findPhenomenonByName(name: String?): Phenomenon? {
    return name?.let { query -> enumValues<Phenomenon>().find { it.nameValue == query } }
}


