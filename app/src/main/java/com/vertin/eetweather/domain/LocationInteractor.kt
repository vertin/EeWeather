package com.vertin.eetweather.domain

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.vertin.eetweather.domain.model.LocationFail
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import java.lang.Exception
import javax.inject.Inject

class LocationInteractor @Inject constructor(@ApplicationContext private val context: Context) {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            numUpdates = 1
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    fun getCurrentLocation(): Single<LatLng> {
        return Single.create { emitter ->

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                emitter.onError(LocationException(LocationFail.PermissionRequired))
            } else {
                try {
                    val result = Tasks.await(fusedLocationClient.lastLocation)
                    if (result != null) {
                        emitter.onSuccess(
                            LatLng(result.latitude, result.longitude)
                        )
                    } else {
                        requestUpdates(emitter)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    requestUpdates(emitter)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestUpdates(emitter: @NonNull SingleEmitter<LatLng>) {
        Looper.prepare()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    val latLng = p0?.let {
                        LatLng(
                            it.lastLocation.latitude,
                            it.lastLocation.longitude
                        )
                    }
                    latLng?.let { emitter.onSuccess(it) }
                        ?: run {
                            emitter.onError(
                                LocationException(
                                    LocationFail.Failed(
                                        "Location can't be determined"
                                    )
                                )
                            )
                        }
                }
            }, null
        )
    }

//                fusedLocationClient.lastLocation.addOnSuccessListener {
//                    if (it != null) {
//                        emitter.onSuccess(
//                            LatLng(it.latitude, it.longitude)
//                        )
//                    } else {
//                        fusedLocationClient.requestLocationUpdates(
//                            locationRequest,
//                            object : LocationCallback() {
//                                override fun onLocationResult(p0: LocationResult?) {
//                                    val latLng = p0?.let {
//                                        LatLng(
//                                            it.lastLocation.latitude,
//                                            it.lastLocation.longitude
//                                        )
//                                    }
//                                    latLng?.let { emitter.onSuccess(it) }
//                                        ?: run {
//                                            emitter.onError(
//                                                LocationException(
//                                                    LocationFail.Failed(
//                                                        "Location can't be determined"
//                                                    )
//                                                )
//                                            )
//                                        }
//                                }
//                            }, null
//                        )
//                    }
//                }.addOnFailureListener {
//                    emitter.onError(
//                        LocationException(
//                            LocationFail.Failed(
//                                "Location can't be determined"
//                            )
//                        )
//                    )
//                }
}