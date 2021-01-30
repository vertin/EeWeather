package com.vertin.eetweather.domain.model

sealed class LocationFail(msg:String):InternalFail(msg) {
    object PermissionRequired : LocationFail("")
    class Failed(msg:String) : LocationFail(msg)
}