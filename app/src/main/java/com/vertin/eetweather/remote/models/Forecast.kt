/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: Weather API
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package com.vertin.eetweather.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @property date
 * @property day
 * @property night
 */
@JsonClass(generateAdapter = true)
data class Forecast(
    @Json(name = "date") @field:Json(name = "date") var date: String,
    @Json(name = "day") @field:Json(name = "day") var day: Day,
    @Json(name = "night") @field:Json(name = "night") var night: Day
)
