package com.vertin.eetweather.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.vertin.eetweather.domain.model.PlacePreview
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(
    private val prefs: SharedPreferences,
    private val moshi: Moshi
) :
    PlacePreferences, FeaturePreferences {
    companion object {
        const val ASSIGNED_PLACE = "ASSIGED_PLACE"
    }

    override fun resetAssignedPlace(): Completable {
        return Completable.fromCallable {
            prefs.edit().remove(ASSIGNED_PLACE).commit()
        }
    }

    @SuppressLint("ApplySharedPref")
    override fun assignToPlace(place: PlacePreview): Completable = Completable.fromAction {
        val placeJson = moshi.adapter(PlacePreview::class.java).toJson(place)
        prefs.edit().putString(ASSIGNED_PLACE, placeJson).commit()
    }

    override fun getAssignedPlace(): Single<PlacePreview> = Single.fromCallable {
        val placeJson = prefs.getString(ASSIGNED_PLACE, "")
        if (placeJson!!.isEmpty()) {
            throw NoSuchElementException()
        } else {
            return@fromCallable moshi.adapter(PlacePreview::class.java).fromJson(placeJson)
        }
    }

    override fun enableFeature(feature: String, enable: Boolean): Completable =
        Completable.fromCallable {
            prefs.edit().putBoolean(feature, enable).commit()
        }

    override fun isFeatureEnabled(feature: String): Single<Boolean> {
        return Single.fromCallable {
            prefs.getBoolean(feature, true)
        }
    }
}