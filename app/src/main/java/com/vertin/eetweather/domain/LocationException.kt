package com.vertin.eetweather.domain

import com.vertin.eetweather.domain.model.LocationFail

class LocationException(val locationFail: LocationFail) : Throwable()