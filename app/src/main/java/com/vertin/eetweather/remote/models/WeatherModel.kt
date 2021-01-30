/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: Weather API
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package com.vertin.eetweather.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @property weatherDateGmt
 * @property locations
 */
@JsonClass(generateAdapter = true)
data class WeatherModel(
    @Json(name = "weatherDateGmt") @field:Json(name = "weatherDateGmt") var weatherDateGmt: String? = null,
    @Json(name = "locations") @field:Json(name = "locations") var locations: List<LocationDetails>? = null
)