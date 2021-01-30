/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: Weather API
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package com.vertin.eetweather.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @property code
 * @property nameEst
 * @property nameEng
 * @property wind
 * @property winddirection
 * @property temperature
 * @property dewpoint
 * @property relativehumidity
 * @property precipitations
 * @property precipitationsperiod
 * @property presentweather
 * @property pressure
 * @property pressuretendency
 * @property pressurechange
 * @property cloudcovertotal
 * @property cloudcoverlow
 * @property cloudbase
 * @property cloudtypelow
 * @property cloudtypemed
 * @property cloudtypehigh
 * @property visibility
 * @property mode
 */
@JsonClass(generateAdapter = true)
data class LocationDetails(
    @Json(name = "code") @field:Json(name = "code") var code: String? = null,
    @Json(name = "name_est") @field:Json(name = "name_est") var nameEst: String? = null,
    @Json(name = "name_eng") @field:Json(name = "name_eng") var nameEng: String? = null,
    @Json(name = "wind") @field:Json(name = "wind") var wind: ElementWithUnit? = null,
    @Json(name = "winddirection") @field:Json(name = "winddirection") var winddirection: ElementWithUnit? = null,
    @Json(name = "temperature") @field:Json(name = "temperature") var temperature: ElementWithUnit? = null,
    @Json(name = "dewpoint") @field:Json(name = "dewpoint") var dewpoint: ElementWithUnit? = null,
    @Json(name = "relativehumidity") @field:Json(name = "relativehumidity") var relativehumidity: ElementWithUnit? = null,
    @Json(name = "precipitations") @field:Json(name = "precipitations") var precipitations: ElementWithUnit? = null,
    @Json(name = "precipitationsperiod") @field:Json(name = "precipitationsperiod") var precipitationsperiod: ElementWithUnit? = null,
    @Json(name = "presentweather") @field:Json(name = "presentweather") var presentweather: String? = null,
    @Json(name = "pressure") @field:Json(name = "pressure") var pressure: ElementWithUnit? = null,
    @Json(name = "pressuretendency") @field:Json(name = "pressuretendency") var pressuretendency: String? = null,
    @Json(name = "pressurechange") @field:Json(name = "pressurechange") var pressurechange: ElementWithUnit? = null,
    @Json(name = "cloudcovertotal") @field:Json(name = "cloudcovertotal") var cloudcovertotal: String? = null,
    @Json(name = "cloudcoverlow") @field:Json(name = "cloudcoverlow") var cloudcoverlow: String? = null,
    @Json(name = "cloudbase") @field:Json(name = "cloudbase") var cloudbase: String? = null,
    @Json(name = "cloudtypelow") @field:Json(name = "cloudtypelow") var cloudtypelow: String? = null,
    @Json(name = "cloudtypemed") @field:Json(name = "cloudtypemed") var cloudtypemed: String? = null,
    @Json(name = "cloudtypehigh") @field:Json(name = "cloudtypehigh") var cloudtypehigh: String? = null,
    @Json(name = "visibility") @field:Json(name = "visibility") var visibility: String? = null,
    @Json(name = "mode") @field:Json(name = "mode") var mode: String? = null
)
